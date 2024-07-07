public class Test {

    @org.junit.jupiter.api.Test
    public void numberRegexTest() {
        String regex = "[\\d\\.]";
        System.out.println(".".matches(regex));
        double a = 1234567891234567D;
        double r = Math.pow(a,2);
        System.out.println("r = " + r);
        System.out.println(Double.parseDouble("156.655"));
    }
}
