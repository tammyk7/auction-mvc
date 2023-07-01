package com.weareadaptive.cluster.clusterUtil;

import java.util.ArrayList;
import java.util.List;

import io.aeron.cluster.service.ClientSession;

/**
 * Manages client sessions within the cluster
 */
public class ClientSessionManager
{
    private final List<ClientSession> allSessions = new ArrayList<>();

    /**
     * Adds a client session
     *
     * @param session the session to add
     */
    public void addSession(final ClientSession session)
    {
        allSessions.add(session);
    }

    /**
     * Removes a client session
     *
     * @param session the session to remove
     */
    public void removeSession(final ClientSession session)
    {
        allSessions.remove(session);
    }

    /**
     * Gets all client sessions known
     *
     * @return the list of client sessions
     */
    public List<ClientSession> getAllSessions()
    {
        return allSessions;
    }
}

