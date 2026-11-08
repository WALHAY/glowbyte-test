package git.walhay.presentation.dto;

import git.walhay.application.dto.TaskUpdateCommand;

public record TaskUpdateDto(String name, String description, Boolean completed) {
	public TaskUpdateCommand toCommand() {
		return new TaskUpdateCommand(name, description(), completed());
	}
}
