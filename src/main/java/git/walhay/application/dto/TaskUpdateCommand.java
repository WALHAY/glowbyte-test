package git.walhay.application.dto;

public record TaskUpdateCommand(String name, String description, Boolean completed) {}
