package git.walhay.repository;

import git.walhay.application.exception.TaskNotFoundException;
import git.walhay.domain.model.Task;
import git.walhay.domain.repository.TaskRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JdbcTaskRepositoryImpl implements TaskRepository {

	@Override
	public Task save(Task task) {
		return null;
	}

    @Override
    public Task update(Task task) {
        return null;
    }

	@Override
	public void deleteById(UUID id) {
		
	}

	@Override
	public Task findTaskById(UUID id) throws TaskNotFoundException {
		return null;
	}

	@Override
	public List<Task> findAllTasks(Boolean completed) {
		return List.of();
	}
}
