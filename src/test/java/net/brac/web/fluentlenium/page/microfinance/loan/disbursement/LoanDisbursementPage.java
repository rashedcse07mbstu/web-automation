package net.brac.web.fluentlenium.page.microfinance.loan.disbursement;

import net.brac.web.fluentlenium.component.microfinance.loan.proposal.ApprovedLoanItem;
import net.brac.web.fluentlenium.page.microfinance.BaseMicroFinancePage;
import net.brac.web.fluentlenium.util.GeneralUtil;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;

import java.util.concurrent.TimeUnit;

public class LoanDisbursementPage extends BaseMicroFinancePage<LoanDisbursementPage> {
    @Override
    protected String projectSelector() {
        return "projectInfoId";
    }

    @Override
    public String getPageId() {
        return null;
    }

    @Override
    public String getUrl() {
        return "/mfDashboard#!/loanDisbursement/approvedLoanList";
    }

    public LoanDisbursementPage clickSearchBtn() {
        FluentWebElement fluentWebElementSearchBtn = el("input.ui-state-default[name='create']");
        fluentWebElementSearchBtn.scrollToCenter().waitAndClick();
        return this;
    }

    public FluentList<FluentWebElement> getApprovedLoanList() {
        waitForDataLoading();
        FluentList<FluentWebElement> fluentWebElementsLoan = find("table#loan-approved-grid tbody tr[id]");
        return fluentWebElementsLoan;
    }

    public ApprovedLoanItem getFirstLoanItem() {
        return getApprovedLoanList().get(0).as(ApprovedLoanItem.class);
    }

    public LoanDisbursementPage submitDisburse() {
        FluentWebElement element = el("input#disburseButtonId");
        await().atMost(TIME_OUT_DURATION, TimeUnit.SECONDS).untilPage().isLoaded();
        await().atMost(30, TimeUnit.SECONDS).until(element).clickable();
        element.scrollToCenter().waitAndClick();
        GeneralUtil.waitForDomStable();
        alert().accept();
        waitForSubmissionLoader();
        return this;
    }
}
