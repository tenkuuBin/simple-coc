package com.github.bin.service;

import com.github.bin.command.Command;
import com.github.bin.config.MsgDataSource;
import com.github.bin.mapper.msg.HisMsgMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author bin
 * @since 2023/08/22
 */
@Service
public class HisMsgService {
    private static HisMsgMapper hisMsgMapper;
    private static List<Command> commands;

    @Autowired
    public void setHisMsgMapper(HisMsgMapper hisMsgMapper) {
        HisMsgService.hisMsgMapper = hisMsgMapper;
    }

    @Autowired
    public void setCommands(List<Command> commands) {
        HisMsgService.commands = commands;
    }

    public static <T> T apply(String tableName, Function<HisMsgMapper, T> block) {
        MsgDataSource.set(tableName);
        return block.apply(hisMsgMapper);
    }

    public static void accept(String tableName, Consumer<HisMsgMapper> block) {
        MsgDataSource.set(tableName);
        block.accept(hisMsgMapper);
    }

    public static void handleBot(RoomConfig roomConfig, String id, String msg) {
        for (val command : commands) {
            if (command.invoke(roomConfig, id, msg)) {
                return;
            }
        }
    }

    public static void sendAsBot(RoomConfig roomConfig, String msg) {
        roomConfig.sendAsBot(msg);
    }
}