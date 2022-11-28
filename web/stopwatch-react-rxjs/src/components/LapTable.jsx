import React from "react";
import { formatTime } from "../utils";

export default function LapTable({ laps, minLap, maxLap }) {
  return (
    <table>
      <tbody>
        {laps.map((lap) => (
          <tr key={lap.lapNumber}>
            <td>Lap {lap.lapNumber}</td>
            <td>{formatTime(lap.time)}</td>
            {laps.length > 2 ? (
              <td>
                {lap.lapNumber === minLap.lapNumber ? "min" : null}
                {lap.lapNumber === maxLap.lapNumber ? "max" : null}
              </td>
            ) : null}
          </tr>
        ))}
      </tbody>
    </table>
  );
}
