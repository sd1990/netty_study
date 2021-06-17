package org.songdan.netty.mt;

/**
 * @author: Songdan
 * @create: 2021-06-17 15:12
 **/
public class LoginResponsePacket extends Packet {


    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
