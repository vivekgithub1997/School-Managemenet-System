package com.school.mgnt.sys.student.management.parent;

public interface ParentServices {

	ParentResponse addParent(ParentRequest parentRequest, long registrationId, int schoolId);

}
