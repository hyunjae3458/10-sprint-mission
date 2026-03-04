package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(
        name = "discodeit.repository.type",
        havingValue = "jcf",
        matchIfMissing = true
)
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository(){
        this.data = new HashMap<>();
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {

        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(rs-> rs.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(rs-> rs.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByUserIdChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(rs-> rs.getChannelId().equals(channelId)&&rs.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void save(ReadStatus readStatus) {
        data.put(readStatus.getId(),readStatus);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
