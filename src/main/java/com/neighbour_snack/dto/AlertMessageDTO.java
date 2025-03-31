package com.neighbour_snack.dto;

public record AlertMessageDTO(MessageType type, String message) {
    public enum MessageType {
        DANGER, SUCCESS, WARNING
    }
}
