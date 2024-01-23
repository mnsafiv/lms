package ru.safonoviv.lms.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.safonoviv.lms.service.BookFeedbackService;
import ru.safonoviv.lms.util.PrivilegeUtil;

@Service
@RequiredArgsConstructor
public class BookFeedbackServiceImpl implements BookFeedbackService {
    private final PrivilegeUtil privilegeUtil;
}
