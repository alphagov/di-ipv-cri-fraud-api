package uk.gov.di.ipv.cri.fraud.api.util;

import uk.gov.di.ipv.cri.common.library.domain.personidentity.Address;
import uk.gov.di.ipv.cri.common.library.domain.personidentity.AddressType;
import uk.gov.di.ipv.cri.common.library.domain.personidentity.PersonIdentity;
import uk.gov.di.ipv.cri.fraud.api.gateway.dto.response.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static uk.gov.di.ipv.cri.common.library.domain.personidentity.AddressType.CURRENT;

public class TestDataCreator {
    public static PersonIdentity createTestPersonIdentity(AddressType addressType) {
        PersonIdentity personIdentity = new PersonIdentity();
        personIdentity.setDateOfBirth(LocalDate.of(1976, 12, 26));
        Address address = new Address();
        address.setValidFrom(LocalDate.now().minusYears(3));
        if (addressType.equals(AddressType.PREVIOUS)) {
            address.setValidUntil(LocalDate.now().minusMonths(1));
        }

        address.setPostalCode("Postcode");
        address.setStreetName("Street Name");
        address.setAddressLocality("PostTown");
        personIdentity.setAddresses(List.of(address));
        return personIdentity;
    }

    public static PersonIdentity createTestPersonIdentity() {
        return createTestPersonIdentity(CURRENT);
    }

    public static IdentityVerificationResponse createTestVerificationInfoResponse() {
        IdentityVerificationResponse testIVR = new IdentityVerificationResponse();

        ResponseHeader header = new ResponseHeader();
        header.setRequestType("TEST_INFO_REQUEST");
        header.setClientReferenceId("1234567890abcdefghijklmnopqrstuvwxyz");
        header.setExpRequestId("1234");
        header.setMessageTime("2022-01-01T00:00:01Z");

        OverallResponse overallResponse = new OverallResponse();
        overallResponse.setDecision("OK");
        overallResponse.setDecisionText("OK");
        overallResponse.setDecisionReasons(List.of("NoReason"));
        overallResponse.setRecommendedNextActions(List.of(""));
        overallResponse.setSpareObjects(List.of(""));
        header.setOverallResponse(overallResponse);

        header.setResponseCode("1234");
        header.setResponseType(ResponseType.INFO);
        header.setResponseMessage("Text");
        header.setTenantID("1234");

        testIVR.setResponseHeader(header);

        ClientResponsePayload payload = new ClientResponsePayload();

        List<OrchestrationDecision> orchestrationDecisions = new ArrayList<>();
        OrchestrationDecision orchestrationDecision1 = new OrchestrationDecision();
        orchestrationDecision1.setSequenceId("1");
        orchestrationDecision1.setDecisionSource("Test");
        orchestrationDecision1.setDecision("OK");
        orchestrationDecision1.setDecisionReasons(List.of("Test"));
        orchestrationDecision1.setScore(0);
        orchestrationDecision1.setDecisionText("Test");
        orchestrationDecision1.setDecisionTime("2022-01-01T00:00:02Z");
        orchestrationDecision1.setNextAction("Continue");
        orchestrationDecision1.setAppReference("UNIT_TEST");
        orchestrationDecision1.setDecisionReasons(List.of("Test"));

        orchestrationDecisions.add(orchestrationDecision1);
        payload.setOrchestrationDecisions(orchestrationDecisions);

        List<DecisionElement> decisionElements = new ArrayList<>();
        DecisionElement decisionElement1 = new DecisionElement();
        decisionElement1.setApplicantId("APPLICANT_1");
        decisionElement1.setServiceName("Authenticateplus");
        decisionElement1.setDecision("AU01");
        decisionElement1.setScore(90);
        decisionElement1.setDecisionText("OK");
        decisionElement1.setDecisionReason("TEST");
        decisionElement1.setAppReference("UNIT_TEST");

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setRuleName("AUTP_IDCONFLEVEL");
        rule1.setRuleId("");
        rule1.setRuleScore(1);
        rule1.setRuleText("Conf Level 1");

        rules.add(rule1);
        decisionElement1.setRules(rules);

        decisionElements.add(decisionElement1);
        payload.setDecisionElements(decisionElements);

        testIVR.setClientResponsePayload(payload);

        return testIVR;
    }
}
