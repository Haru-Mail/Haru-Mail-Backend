package com.project.Haru_Mail.domain.diary;
import java.io.Serializable;
import java.util.Objects;

//DiaryTag 복합키 처리용
public class DiaryTagId implements Serializable {

    private Diary diary;  // Diary id
    private Tag tag;    // Tag id

    public DiaryTagId() {}

    public DiaryTagId(Diary diary, Tag tag) {
        this.diary = diary;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiaryTagId that)) return false;
        return Objects.equals(diary, that.diary) &&
                Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diary, tag);
    }
}
