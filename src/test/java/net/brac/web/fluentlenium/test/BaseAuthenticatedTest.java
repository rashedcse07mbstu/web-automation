package net.brac.web.fluentlenium.test;

import net.brac.web.fluentlenium.page.BasePage;
import net.brac.web.fluentlenium.page.LoginPage;
import net.brac.web.fluentlenium.util.Credential;
import net.brac.web.fluentlenium.util.CredentialException;
import org.apache.commons.lang3.StringUtils;
import org.fluentlenium.core.FluentPage;

import java.util.Objects;

public abstract class BaseAuthenticatedTest extends BaseTest {
    protected abstract Credential credential();

    public <P extends FluentPage> P goTo(P page) {
        Credential credential = credential();
        if (Objects.isNull(credential)) {
            throw new CredentialException("Credential is null");
        }
        if (StringUtils.isBlank(page.getBaseUrl()) && StringUtils.isNotBlank(page.getUrl())) {
            super.setBaseUrl(credential().getBaseUrl());
        }
        if (page instanceof BasePage) {
            BasePage basePage = super.goTo((BasePage) page);
            String currentPageId = basePage.currentPageId();
            if (!isSamePage(basePage.getPageId(), currentPageId) && isAnyLoginPage(currentPageId)) {
                if (isSamePage(LoginPage.PAGE_ID, currentPageId)) {
                    LoginPage loginPage = newInstance(LoginPage.class);
                    loginPage.fillAndSubmitForm(credential.getUsername(), credential.getPassword());
                    if (loginPage.hasOffice()) {
                        loginPage.selectOffice(credential.getOfficeCode());
                    }
                } else {
                    throw new IllegalStateException("expected to be at LoginPage or CommunityLoginPage, but actually not");
                }
            } else {
                return page;
            }
        }
        return super.goTo(page);
    }

    public <P extends FluentPage> P goTo(Class<P> pageClass) {
        return goTo(newInstance(pageClass));
    }

    private boolean isAnyLoginPage(String pageId) {
        return StringUtils.equalsAny(pageId, LoginPage.PAGE_ID);
    }

    private boolean isSamePage(String pageId, String currentPageId) {
        return StringUtils.equals(pageId, currentPageId);
    }
}
