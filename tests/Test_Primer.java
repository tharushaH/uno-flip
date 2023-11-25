import org.junit.After;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CardTest.class,
        DeckTest.class,
        DrawFiveTest.class,
        DrawOneTest.class,
        FlipTest.class,
        HandTest.class,
        NumberTest.class,
        PlayerTest.class,
        ReverseTest.class,
        SelfDrawOneTest.class,
        SkipEveryoneTest.class,
        SkipTest.class,
        UnoFlipModelTest.class,
        WildDrawTwoTest.class,
        WildTest.class
})

public class Test_Primer {

    public static void main(String[] args) {
        JUnitCore.runClasses(Test_Primer.class);
    }

}
