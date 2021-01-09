package com.untact.persistent.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.untact.attendance.persistent.AttendanceRepository;
import com.untact.board.persistent.BoardRepository;
import com.untact.englishdictionary.persistent.EnglishDictionaryRepository;
import com.untact.englishspelling.persistent.EnglishSpellingRepository;
import com.untact.file.persistent.FileEntityRepository;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.groupinclude.persistent.GroupIncludeRepository;
import com.untact.member.persistent.MemberEntityRepository;
import com.untact.reply.persistent.ReplyRepository;
import com.untact.representativetimetableitem.persistent.RepresentativeTimeTableItemRepository;
import com.untact.timetable.persistent.TimeTableRepository;
import com.untact.timetableitem.persistent.TimeTableItemRepository;
import com.untact.vocabulary.persistent.VocabularyRepository;

@Component
public class DeleteAllUtil {
	@Autowired
	private AttendanceRepository attendanceRepo;
	@Autowired
	private BoardRepository boardRepo;
	@Autowired
	private EnglishDictionaryRepository englishDictionaryRepo;
	@Autowired
	private EnglishSpellingRepository englishSpellingRepo;
	@Autowired
	private FileEntityRepository fileRepo;
	@Autowired
	private GroupEntityRepository groupRepo;
	@Autowired
	private GroupIncludeRepository groupIncludeRepo;
	@Autowired
	private MemberEntityRepository memberRepo;
	@Autowired
	private ReplyRepository replyRepo;
	@Autowired
	private RepresentativeTimeTableItemRepository representativeTimeTableItemRepo;
	@Autowired
	private TimeTableItemRepository timeTableItemRepo;
	@Autowired
	private TimeTableRepository timeTableRepo;
	@Autowired
	private VocabularyRepository vocabularyRepo;
	public void deleteAllRepo() {
		attendanceRepo.deleteAllInBatch();
		representativeTimeTableItemRepo.deleteAllInBatch();
		timeTableItemRepo.deleteAllInBatch();
		timeTableRepo.deleteAllInBatch();
		replyRepo.deleteAllInBatch();
		fileRepo.deleteAllInBatch();
		boardRepo.deleteAllInBatch();
		groupIncludeRepo.deleteAllInBatch();
		vocabularyRepo.deleteAllInBatch();
		englishDictionaryRepo.deleteAllInBatch();
		englishSpellingRepo.deleteAllInBatch();
		memberRepo.deleteAllInBatch();
		groupRepo.deleteAllInBatch();
	}
}
