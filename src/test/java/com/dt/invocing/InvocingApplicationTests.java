package com.dt.invocing;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

//@SpringBootTest
//@AutoConfigureMockMvc
class InvocingApplicationTests {

    @Autowired
    private MockMvc mvc;

    // @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content().string(equalTo(
                "Invocing app is currently under development! Stay tuned for updates.")));
    }
    // @Test
    void contextLoads() {
    }

}
