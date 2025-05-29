package com.spring.jwt.UserParts;

import com.spring.jwt.SparePart.PaginatedResponse;


public interface UserPartService {

    UserPartDto getUserPartById(Integer userPartId);

    public PaginatedResponse<UserPartDto> getAllUserParts(int page, int size);

    Integer getQuantityByPartNumber(String partNumber);

    UserPartDto updateUserPartAndSparePart(UpdateUserPartRequestDTO request);
    
    void deleteUserPartAndSparePart(Integer userPartId);
}
