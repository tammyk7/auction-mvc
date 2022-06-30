import React from "react";

export default function Buttons({
  isRunning,
  isAtZero,
  resetAction,
  lapAction,
  startAction,
  stopAction,
}) {
  const lapResetText = isRunning || isAtZero ? "Lap" : "Reset";
  const stopStartText = isRunning ? "Stop" : "Start";
  const onLapResetClick = isRunning ? lapAction : resetAction;
  const onStartStopClick = isRunning ? stopAction : startAction;

  return (
    <div>
      <button onClick={onLapResetClick} disabled={isAtZero}>
        {lapResetText}
      </button>
      <button onClick={onStartStopClick}>{stopStartText}</button>
    </div>
  );
}
