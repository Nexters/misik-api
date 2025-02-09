INSERT INTO prompt (id, style, command, created_at, modified_at) VALUES
                                                                    (1, 'PROFESSIONAL', '자연스러운, 전문적인 말투, 존대말, 과거형으로 리뷰를 만들어줘. 응답 형식은 {“review”:”리뷰 내용”}이어야 해. 응답에는 해당 JSON만 있어야해. 또한, 리뷰는 공백을 포함해서 300자가 넘어야 해. 리뷰에는 다음 의견도 자연스럽게 넣어줘.', NOW(), NOW()),
                                                                    (2, 'FRIENDLY', '~다, ~요를 적절히 섞은 높임말로 리뷰를 만들어줘. 한국 중년처럼 보이도록 가끔 문장 끝에 ..같은 특수문자를 넣어줘. 응답 형식은 {“review”:”리뷰 내용”}이어야 해. 응답에는 해당 JSON만 있어야해. 또한, 리뷰는 공백을 포함해서 100자 이상 200자 이하여야해. 리뷰에는 다음 의견도 자연스럽게 넣어줘.', NOW(), NOW()),
                                                                    (3, 'CUTE', '~다, ~요를 적절히 섞은 높임말로 리뷰를 만들어줘. 깜찍한 10대처럼 보이도록 가끔 어미 뒤에 랜덤하게 😘, ㅎㅎ 같은 특수문자, "당, 용" 같이 끝나는 어미를 넣어줘. 응답 형식은 {"review":"리뷰 내용"}이어야 해. 응답에는 해당 JSON만 있어야 해. 또한, 리뷰는 공백을 포함해서 100자 이상 200자 이하여야 해. 리뷰에는 다음 의견도 자연스럽게 넣어줘.', NOW(), NOW());

create table if not exists misikapi.prompt
(
    created_at  datetime(6) null,
    id          bigint      not null
        primary key,
    modified_at datetime(6) null,
    command     text        not null,
    style       varchar(20) not null,
    constraint UKn1rq77g5p76xoejtkn8xgj207
        unique (style)
);