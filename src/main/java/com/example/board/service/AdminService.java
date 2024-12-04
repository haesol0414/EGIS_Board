package com.example.board.service;

import java.util.Map;

public interface AdminService {
    Map<String, Object> getAdminBoardList(String filter, String keyword, int size, int offset);
}
