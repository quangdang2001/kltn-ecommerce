package com.example.kltn.controllers.customer;


import com.example.kltn.constants.TokenType;
import com.example.kltn.dto.LoginRequest;
import com.example.kltn.dto.PasswordDTO;
import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.models.User;
import com.example.kltn.repos.UserRepo;
import com.example.kltn.services.common.iplm.UserSrvIplm;
import com.example.kltn.services.email.EmaiType;
import com.example.kltn.services.email.EmailSenderService;
import freemarker.template.TemplateException;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserSrvIplm userService;
    private final EmailSenderService emailSenderService;

    final String TITLE_SUBJECT_EMAIL = "Shop Register TOKEN";
    final String RESET_PASSWORD_TOKEN = "Reset Password Token";

    @GetMapping("/user")
    public ResponseEntity<?> findCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(new ResponseDTO(true, "Success", user));
    }

    @PostMapping("/user/address")
    ResponseEntity<?> addAddressForUser(@RequestBody User.Address address) {
        userService.addAddressForUser(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/register-email")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User userReq) throws MessagingException, TemplateException, IOException {
        log.info("Register - register for user: {}", userReq);

        String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        var verificationToken = new User.VerificationToken(token, TokenType.REGISTER);
        log.info("Register - Token: {}", token);
        userReq.getTokens().add(verificationToken);
        User user = userService.saveRegister(userReq);

        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("title", TITLE_SUBJECT_EMAIL);
        model.put("subject", TITLE_SUBJECT_EMAIL);
        emailSenderService.sendEmail(userReq.getEmail(), model, EmaiType.REGISTER);

        return ResponseEntity.ok(new ResponseDTO(true, "Sending email",
                null));
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<?> verifyRegistration(@RequestParam("token") String token,
                                                @RequestParam("email") String email) {
        User result = userService.validateAndRegister(token, email);
        if (result != null) {
            return ResponseEntity.ok("");
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/resendVerifyToken")
    public ResponseEntity<?> resendVerificationToken(@RequestParam("email") String email,
                                                     @RequestParam("type") String tokenType
    ) throws MessagingException, TemplateException, IOException {

        String verificationToken
                = userService.SendToken(email, TokenType.getTokenType(tokenType));
        Map<String, Object> model = new HashMap<>();
        model.put("token", verificationToken);
        model.put("title", TITLE_SUBJECT_EMAIL);
        model.put("subject", TITLE_SUBJECT_EMAIL);
        emailSenderService.sendEmail(email, model, EmaiType.REGISTER);

        return ResponseEntity.ok(new ResponseDTO(true, "Verification TOKEN Sent",
                null));
    }

    @PostMapping("/registerTest")
    public ResponseEntity<?> registerUserTest(@RequestBody User userReq) throws MessagingException {
        User users = userService.saveAdmin(userReq);

        return ResponseEntity.ok(new ResponseDTO(true, "Success",
                null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        return ResponseEntity.ok(
                new ResponseDTO(true, "Success", userService.login(loginRequest, request))
        );
    }

    @PutMapping(value = "/user/avatar", consumes = {
            "multipart/form-data"})
    public ResponseEntity<?> upAvatar(@RequestParam("img") MultipartFile file) throws IOException {
        String url = userService.upAvartar(file);

        return ResponseEntity.ok().body(new ResponseDTO(true, "Success",
                url));
    }

    @DeleteMapping("/user/address")
    public ResponseEntity<?> deleteAddressUser(@RequestBody String address) {
        userService.deleteAddressUser(address);
        return ResponseEntity.ok().body(new ResponseDTO(true, "Success", null));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String email, HttpServletRequest request) throws MessagingException, TemplateException, IOException {
        User user = userService.findUserByEmail(email);
        if (user != null && user.getEnable()) {
            String token = "";
            token = userService.SendToken(email, TokenType.RESET_PASSWORD);
            Map<String, Object> model = new HashMap<>();
            model.put("token", token);

            model.put("title", RESET_PASSWORD_TOKEN);
            model.put("subject", RESET_PASSWORD_TOKEN);
            //Send email
            emailSenderService.sendEmail(user.getEmail(), model, EmaiType.REGISTER);
            log.info("Reset password: {}",
                    token);
            return ResponseEntity.ok(new ResponseDTO(true, "Sent email reset token",
                    null));
        }
        return ResponseEntity.badRequest().body(new ResponseDTO(false, "Not found email",
                null));
    }

    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(@Valid @RequestBody PasswordDTO passwordDTO) {
        User result = userService.validatePasswordResetToken(passwordDTO.getToken(), passwordDTO.getEmail());
        if (result == null) {

            return ResponseEntity.badRequest().body(new ResponseDTO(false, "Invalid token",
                    null));
        }
        if (!result.getEnable()) {
            return ResponseEntity.ok().body(new ResponseDTO(false, "Email not verify",
                    null));
        }
        userService.changePassword(result, passwordDTO.getNewPassword());
        return ResponseEntity.ok().body(new ResponseDTO(true, "Change password successfully",
                null));
    }
}
