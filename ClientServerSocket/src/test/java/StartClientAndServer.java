import SocketServer.Client;
import SocketServer.ThreadServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StartClientAndServer {
    @BeforeClass
    public static void startServer() throws Exception {
       new ThreadServer();
    }
    @AfterClass
    public static void stopServer() throws Exception {
        String[] ar = {
                "stop"
        };
        Client.main(ar);
    }

    @Test
    public void testAddGood() throws Exception {
        List<String> answers = Arrays.asList("Персона добавлена",
                "Персона добавлена","Персона добавлена");

        String[] ar = {
                "add {\"firstName\":\"Кулишкина\",\"lastName\":\"Алена\",\"age\":\"20\"}",
                "add {\"firstName\":\"Кулишкина\",\"lastName\":\"Елена\",\"age\":\"20\"}",
                "add {\"firstName\":\"Смирнова\",\"lastName\":\"Лидия\",\"age\":\"19\"}"
        };
        List<String> result = Client.main(ar);
        assertEquals(answers, result);
    }

    @Test
    public void testAddBad() throws Exception {
        List<String> answers = Arrays.asList("Персона не добавлена");

        String[] ar = {
                "add vdjvodjvlds"
        };
        List<String> result = Client.main(ar);
        assertEquals(answers, result);
    }

    @Test
    public void testGetGood() throws Exception {
        List<String> answers = Arrays.asList("{\"firstName\":\"Смирнова\",\"lastName\":\"Лидия\",\"age\":19}",
                "{\"firstName\":\"Кулишкина\",\"lastName\":\"Алена\",\"age\":20}");

        String[] ar = {
                "get {\"firstName\":\"Кулишкина\"}",
                "get {\"firstName\":\"Кулишкина\",\"lastName\":\"Алена\",\"age\":\"20\"}"
        };
        List<String> result = Client.main(ar);
        assertEquals(answers, result);
    }

    @Test
    public void testGetBad() throws Exception {
        List<String> answers = Arrays.asList("{}");

        String[] ar = {
                "get vdjvodjvlds"
        };
        List<String> result = Client.main(ar);
        assertEquals(answers, result);
    }

    @Test
    public void testBadRequest() throws Exception {
        List<String> answers = Arrays.asList("Неправильный формат переданной строки");

        String[] ar = {
                "{\"firstName\":\"Смирнова\",\"lastName\":\"Лидия\",\"age\":\"19\"}"
        };
        List<String> result = Client.main(ar);
        assertEquals(answers, result);
    }

    @Test
    public void testNullRequest() throws Exception {
        List<String> answersThree = Arrays.asList();
        String[] ar2 = null;
        List<String> resultThree = Client.main(ar2);
        assertEquals(answersThree, resultThree);
    }

    @Test
    public void testEmptyRequest() throws Exception {
        List<String> answersTwo = Arrays.asList();
        String[] ar1 = {};
        List<String> resultTwo = Client.main(ar1);
        assertEquals(answersTwo, resultTwo);
    }

}
