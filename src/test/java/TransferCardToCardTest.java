import org.junit.jupiter.api.BeforeEach;
import page.DashboardPage;
import page.LoginPage;
import data.DataHelper;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferCardToCardTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var user = DataHelper.getRegisteredUser();
        var verificationPage = loginPage.validLogin(user);
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void transferValidAmountFromFirstToSecond() {

        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amountTransfer = DataHelper.validAmount(secondCardBalance);
        var expectedBalanceFirstCard = firstCardBalance + amountTransfer;
        var expectedBalanceSecondCard = secondCardBalance - amountTransfer;
        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        dashboardPage = transferPage.validTransfer(String.valueOf(amountTransfer), secondCard);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);

    }

    @Test
    void transferInvalidAmountFromFirstToSecond() {

        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amountTransfer = DataHelper.invalidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amountTransfer;
        var expectedBalanceSecondCard = secondCardBalance + amountTransfer;
        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        transferPage.makeTransfer(String.valueOf(amountTransfer), firstCard);
        transferPage.findError("Не хватает денег для перевода. Уменьшите сумму или переведите с другой карты или счёта");
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }
}
