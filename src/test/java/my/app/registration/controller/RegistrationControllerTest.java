package my.app.registration.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.app.registration.exception.EmailNotValidException;
import my.app.registration.exception.TokenNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import my.app.registration.dto.RegistrationRequestDto;
import my.app.registration.email.EmailService;
import my.app.registration.service.RegistrationService;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private EmailService emailService;

    private RegistrationController registrationController;

    @BeforeEach
    void setUp() {
        registrationController = new RegistrationController(registrationService);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    @Test
    void testRegistration_success() throws Exception {
        // Arrange
        RegistrationRequestDto requestDto = new RegistrationRequestDto("test@example.com","password123","password123");
        when(registrationService.register(any(RegistrationRequestDto.class))).thenReturn("email sent");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/v1/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"password123\",\"repeatPassword\":\"password123\"}"));

        // Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", is("email sent")));
        verify(registrationService).register(requestDto);
    }

    @Test
    public void testConfirmRegistration_success() throws Exception {
        // given
        String token = "token";
        when(registrationService.confirmRegistration(token)).thenReturn("success");

        // when-then
        mockMvc.perform(get("/api/v1/registration/confirm")
                        .param("token", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("success"));
    }
    @Test
    public void testRegister_UnsuccessfulRegistration_EmailNotValidException() throws Exception {
        RegistrationRequestDto requestDto = new RegistrationRequestDto("invalid-email", "password", "password");

        mockMvc.perform(post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegister_UnsuccessfulRegistration_PasswordToShortException() throws Exception {
        RegistrationRequestDto requestDto = new RegistrationRequestDto("invalid@gmail.com", "pass", "pass");

        mockMvc.perform(post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}