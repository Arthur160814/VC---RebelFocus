# Rebel Focus — Antigravity Delivery Kit

This folder contains the minimum set of Markdown documents you should use to drive Antigravity (or a similar coding agent) in a controlled, low-drift way.

## What this kit is for

Use this kit when you want an AI coding agent to build **Rebel Focus**, an Android productivity app distributed as a **sideloaded APK**, with **no Play Store release**, **no backend**, and **no enterprise / device-owner mode**.

The goal is not to ask the agent to "build everything" in one go.
The goal is to give the agent:

1. a stable source of truth,
2. a phased implementation roadmap,
3. ready-to-paste prompts,
4. a human operator workflow.

## Files in this kit

### `01_MASTER_SPEC.md`
The main technical source of truth.
Give this file to the agent first.
It defines architecture, modules, constraints, permissions, tools, and non-goals.

### `02_PHASED_IMPLEMENTATION_PLAN.md`
The build order.
Use it to keep the implementation incremental and testable.

### `03_PROMPT_PACK.md`
Ready-to-paste prompts for Antigravity.
This is the operational layer.
Use these prompts phase by phase.

### `04_OPERATOR_FLOW.md`
How **you** should drive the agent.
This file explains the review loop, checkpoints, and how to avoid drift.

## Recommended order of use

1. Give Antigravity the **master spec**.
2. Run the **planning prompt** from the prompt pack.
3. Review its plan against the **phased implementation plan**.
4. Ask it to implement **one phase only**.
5. Require build + tests + summary after each phase.
6. Run the audit prompt before moving to the next major milestone.

## Core operating rules

- Never ask the agent to implement the whole app in one shot.
- Always require it to list the files it plans to modify before coding.
- Always require a build result at the end of each phase.
- Always keep one file as the source of truth: `01_MASTER_SPEC.md`.
- If the agent starts inventing unsupported Android capabilities, stop and re-anchor it to the master spec.

## What the agent must not do

- It must not promise an unbreakable lock on standard Android.
- It must not introduce enterprise-only concepts like device owner, kiosk mode, lock task mode, EMM, or MDM.
- It must not depend on a backend.
- It must not treat Device Admin as the foundation of the app.
- It must not implement scheduling logic that assumes exact alarms are always available.
- It must not make the UI Activity the source of truth for active focus sessions.

## Minimal practical workflow

### First pass
Give the agent:
- `01_MASTER_SPEC.md`
- the planning prompt from `03_PROMPT_PACK.md`

### Second pass
Give the agent:
- `01_MASTER_SPEC.md`
- `02_PHASED_IMPLEMENTATION_PLAN.md`
- the bootstrap prompt

### Ongoing passes
For each phase:
- tell it which phase to implement,
- require it to compile,
- require it to explain what remains,
- do not let it silently jump ahead to future phases.

## Success criteria

This kit is working if the agent:
- produces a compiling Android project early,
- adds functionality incrementally,
- respects Android platform limits,
- does not oversell anti-cheat or anti-uninstall behavior,
- keeps architecture stable while features grow.
