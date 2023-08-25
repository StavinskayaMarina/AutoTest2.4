package page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import lombok.val;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final String balanceOriginal = "баланс: ";
    private final String balanceFinal = " р.";
    public SelenideElement heading = $("[data-test-id=dashboard]");
    public ElementsCollection listCard = $$(".list__item div");

    public DashboardPage() {

        heading.shouldBe(visible);
    }

    public int getCardBalance(DataHelper.InfoCard infoCard) {
        var text = listCard.findBy(text(infoCard.getNumberCard().substring(15))).getText();
        return extractBalance(text);
    }

    public int getCardBalance(int index) {
        var text = listCard.get(index).getText();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransfer(DataHelper.InfoCard infoCard) {
        listCard.findBy(attribute("data-test-id", infoCard.getTestId())).$(".button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        val original = text.indexOf(balanceOriginal);
        val finish = text.indexOf(balanceFinal);
        val value = text.substring(original + balanceOriginal.length(), finish);
        return Integer.parseInt(value);
    }
}
