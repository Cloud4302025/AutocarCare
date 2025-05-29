package com.spring.jwt.UserParts;


import com.spring.jwt.SparePart.PaginatedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userParts")
public class UserPartController {

    private final UserPartService userPartService;

    public UserPartController(UserPartService userPartService) {
        this.userPartService = userPartService;
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getUserPartById(@RequestParam Integer userPartId) {
        try {
            UserPartDto userPart = userPartService.getUserPartById(userPartId);
            return ResponseEntity.ok(userPart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("UserPart not found: " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUserParts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        try {
            PaginatedResponse<UserPartDto> userParts = userPartService.getAllUserParts(page, size);
            return ResponseEntity.ok(userParts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to retrieve user parts: " + e.getMessage());
        }
    }

    @GetMapping("/quantity/{partNumber}")
    public ResponseEntity<Integer> getQuantity(@PathVariable String partNumber) {
        Integer quantity = userPartService.getQuantityByPartNumber(partNumber);
        return ResponseEntity.ok(quantity);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateUserPartAndSparePart(@RequestBody UpdateUserPartRequestDTO request) {
        try {
            UserPartDto updatedUserPart = userPartService.updateUserPartAndSparePart(request);
            return ResponseEntity.ok(updatedUserPart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/delete/{userPartId}")
    public ResponseEntity<?> deleteUserPart(@PathVariable Integer userPartId) {
        try {
            userPartService.deleteUserPartAndSparePart(userPartId);
            return ResponseEntity.ok("User part and associated spare part deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Delete failed: " + e.getMessage());
        }
    }
}

