package uk.gov.di.ipv.cri.fraud.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.di.ipv.cri.fraud.api.persistence.item.ContraindicationMappingItem;
import uk.gov.di.ipv.cri.fraud.library.persistence.DataStore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class ContraindicationMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContraindicationMapper.class);

    private final Map<String, String> contraindicationMappings;
    private final DataStore<ContraindicationMappingItem> dataStore;

    ContraindicationMapper(DataStore<ContraindicationMappingItem> dataStore, String thirdPartyId) {
        this.dataStore = dataStore;
        this.contraindicationMappings = getContraindicationMappings(thirdPartyId);
    }

    String[] mapThirdPartyFraudCodes(String[] thirdPartyFraudCodes) {
        Objects.requireNonNull(thirdPartyFraudCodes, "thirdPartyFraudCodes must not be null");

        if (thirdPartyFraudCodes.length == 0) {
            return new String[] {};
        }

        List<String> contraindicators =
                Arrays.stream(thirdPartyFraudCodes)
                        .filter(this.contraindicationMappings::containsKey)
                        .map(this.contraindicationMappings::get)
                        .collect(Collectors.toList());

        if (contraindicators.size() != thirdPartyFraudCodes.length) {
            String[] unmappedFraudCodes =
                    Arrays.stream(thirdPartyFraudCodes)
                            .filter(fraudCode -> !contraindicators.contains(fraudCode))
                            .toArray(String[]::new);

            String unmappedFraudCodesAsString = String.join(", ", unmappedFraudCodes);

            LOGGER.warn("Unmapped fraud codes encountered: {}", unmappedFraudCodesAsString);
        }

        return contraindicators.toArray(String[]::new);
    }

    private Map<String, String> getContraindicationMappings(String thirdPartyId) {
        List<ContraindicationMappingItem> contraindicationMappingItems =
                dataStore.getItemsByAttribute("thirdPartyId", thirdPartyId);

        return contraindicationMappingItems.stream()
                .collect(
                        Collectors.toMap(
                                ContraindicationMappingItem::getThirdPartyFraudCode,
                                ContraindicationMappingItem::getContraindicationCode));
    }
}
