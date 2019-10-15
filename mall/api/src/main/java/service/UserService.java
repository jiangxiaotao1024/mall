package service;

import bean.Member;
import bean.MemberReceiveAddress;

import java.util.List;

public interface UserService {
    public Member login(Member member);

    void addUserToken(String token, String memberId);

    Member checkMember(Member check);

    void addUser(Member member);

    List<MemberReceiveAddress> getReceiveAddress(String memberId);

    MemberReceiveAddress getReceiveAddressById(String receiveAddressId);
}
