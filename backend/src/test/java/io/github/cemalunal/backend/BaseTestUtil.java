package io.github.cemalunal.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@ActiveProfiles("test")
public class BaseTestUtil<T> {

    private MockMvc mockMvc;
    private Gson gson = new Gson();

    @Autowired
    public BaseTestUtil(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    public MvcResult sendPostAndExpectSuccessful(T dto, String path, Object... variables) throws Exception {
        return mockMvc.perform(post(path, variables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful()).andReturn();
    }

    public MvcResult sendGetAndExpectOk(String path, Object... variables) throws Exception {
        return mockMvc.perform(get(path, variables))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    public void sendDeleteAndExpectOk(String path, Object... variables) throws Exception {
        mockMvc.perform(delete(path, variables))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    public void sendDeleteAndExpectNoContent(String path, Object... variables) throws Exception {
        mockMvc.perform(delete(path, variables))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent()).andReturn();
    }

    public String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public T objectFromString(String userString, Class<T> clazz) {
        JsonObject jsonObject = gson.fromJson(userString, JsonObject.class);
        JsonElement jsonElement = jsonObject.get("data");
        return gson.fromJson(jsonElement, clazz);
    }

    public Gson getGson() {
        return gson;
    }

}
