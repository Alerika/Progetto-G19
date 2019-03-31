import it.unipv.gui.login.User;
import it.unipv.gui.login.UserInfo;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LoginTest extends TestCase {

    @Test
    public void test() {
        User u = UserInfo.getUserInfo();
        assertEquals(u.getName(), "Admin");
        assertEquals(u.getPassword(), "Admin");
    }
}
