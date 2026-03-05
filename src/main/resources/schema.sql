-- 1. Enum 타입 생성
CREATE TYPE ChannelType AS ENUM ('PRIVATE', 'PUBLIC');

-- 2.  바이너리 파일을 관리하는 테이블
CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    file_name    varchar(50),
    size         bigint,
    content_type varchar(50),
    bytes        bytea,
    created_at   timestamp NOT NULL
);

-- 3. 채널을 관리하는 테이블
CREATE TABLE channels
(
    id           UUID PRIMARY KEY,
    channel_name varchar(50),
    description  varchar(500),
    channel_type ChannelType NOT NULL,
    created_at   timestamp   NOT NULL,
    updated_at   timestamp
);

-- 4. 유저를 관리하는 테이블
CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    user_name  varchar(50) UNIQUE  NOT NULL,
    email      varchar(100) UNIQUE NOT NULL,
    password   varchar(60)         NOT NULL,
    profile_id UUID                REFERENCES binary_contents (id) ON DELETE SET NULL,
    created_at timestamp           NOT NULL,
    updated_at timestamp
);

-- 5. 유저 온라인 상태 확인 테이블
CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    user_id        UUID UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    last_online_at timestamp,
    created_at     timestamp NOT NULL,
    updated_at     timestamp
);

-- 6. 메시지 관리 테이블
CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    text       text,
    author_id  UUID      REFERENCES users (id) ON DELETE SET NULL,
    channel_id UUID      NOT NULL REFERENCES channels (id) ON DELETE CASCADE,
    created_at timestamp NOT NULL,
    updated_at timestamp
);

-- ⭐️ 7. 메시지와 바이너리 컨텐츠를 연관관계를 관리하는 조인 테이블
-- message, user, binary-content의 참조 순환을 해결!
CREATE TABLE message_attachments
(
    message_id        UUID NOT NULL REFERENCES messages (id) ON DELETE CASCADE,
    binary_content_id UUID NOT NULL REFERENCES binary_contents (id) ON DELETE CASCADE,
    PRIMARY KEY (message_id, binary_content_id)
);

-- 8. 읽음 시간 관리 테이블
CREATE TABLE read_statuses
(
    id             UUID PRIMARY KEY,
    last_read_time timestamp NOT NULL,
    author_id      UUID      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    channel_id     UUID      NOT NULL REFERENCES channels (id) ON DELETE CASCADE,
    created_at     timestamp NOT NULL,
    updated_at     timestamp,
    CONSTRAINT unique_author_channel UNIQUE (author_id, channel_id)
);