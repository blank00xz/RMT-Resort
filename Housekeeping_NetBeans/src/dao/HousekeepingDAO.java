/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import adt.QueueInterface;
import adt.ResortQueue;
import entity.RoomTaskLog;
import java.time.LocalDateTime;

public final class HousekeepingDAO {

    private HousekeepingDAO() {
    }

    public static QueueInterface<RoomTaskLog> getSampleTaskLogs() {

        QueueInterface<RoomTaskLog> queue =
                new ResortQueue<>();

        LocalDateTime now =
                LocalDateTime.now();

        queue.enqueue(
                new RoomTaskLog(
                        "101",
                        RoomTaskLog.STATUS_DIRTY,
                        "Ali",
                        now.minusMinutes(120)));

        queue.enqueue(
                new RoomTaskLog(
                        "102",
                        RoomTaskLog.STATUS_CLEANING,
                        "Siti",
                        now.minusMinutes(75)));

        queue.enqueue(
                new RoomTaskLog(
                        "103",
                        RoomTaskLog.STATUS_INSPECTED,
                        "Wei Ming",
                        now.minusMinutes(40)));

        queue.enqueue(
                new RoomTaskLog(
                        "104",
                        RoomTaskLog.STATUS_READY,
                        "Kumar",
                        now.minusMinutes(150)));

        queue.enqueue(
                new RoomTaskLog(
                        "105",
                        RoomTaskLog.STATUS_DIRTY,
                        "Ali",
                        now.minusMinutes(15)));

        return queue;
    }
}