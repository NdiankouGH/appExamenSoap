package com.examensaop.service;

import com.examensoap.generated.Classes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ClassesService {

    private final Map<Long, Classes> students = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);


}
