import com.jayway.restassured.RestAssured;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.welflex.spring.web.notes.Application;
import com.welflex.spring.web.notes.Note;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static com.jayway.restassured.RestAssured.baseURI;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", // Pick an ephemeral port
                  "eureka.client.enabled=false" // Not registering/discovering with Eureka
})
public class NotesControllerTest {
  @Value("${local.server.port}")
 private  int port;

  @Before
  public void setUp() {
    RestAssured.port = port;
  }

  @Test
  public void createAndGetNote() {
    given().contentType(MediaType.APPLICATION_XML)
           .body(new Note("Test"))
            .when()
            .post("http://localhost:" + port + "/notes")
            .then()
            .statusCode(200).body(equalTo("1"));

    when().get("/notes/{id}", 1).then()
            .assertThat()
            .statusCode(200)
             .body("note.content", equalTo("Test"));
  }
}
