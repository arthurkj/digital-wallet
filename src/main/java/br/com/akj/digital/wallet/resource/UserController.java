package br.com.akj.digital.wallet.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.akj.digital.wallet.dto.UserCreationRequest;
import br.com.akj.digital.wallet.dto.UserResponse;
import br.com.akj.digital.wallet.service.user.UserCreationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserCreationService userCreationService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody final UserCreationRequest request) {
        return ResponseEntity.ok(userCreationService.create(request));
    }
}
