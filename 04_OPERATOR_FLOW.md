# Rebel Focus — Operator Flow for Antigravity

This file explains how **you** should drive the coding agent.

The best results usually come from treating the agent as a strong implementer, not as an unsupervised product owner.

## 1. The operating model

Think of the flow as:

1. **specify**,
2. **plan**,
3. **implement one phase**,
4. **verify**,
5. **review**,
6. **continue or correct**.

Do not collapse those steps into a single request.

## 2. Your role

Your job is not to micro-manage every line of code.
Your job is to:
- keep the source of truth stable,
- approve or reject implementation direction,
- stop drift early,
- require proof of progress,
- ensure the build stays green.

## 3. The recommended execution loop

### Step A — Anchor the agent
Give it:
- `01_MASTER_SPEC.md`
- Prompt 1 from `03_PROMPT_PACK.md`

What you want back:
- architecture summary,
- proposed phases,
- risk list,
- file and module plan,
- blockers if any.

### Step B — Compare the plan
Compare the agent's plan against:
- `01_MASTER_SPEC.md`
- `02_PHASED_IMPLEMENTATION_PLAN.md`

Check for:
- enterprise drift,
- backend drift,
- impossible Android claims,
- overuse of permissions,
- big-bang implementation.

### Step C — Approve only one phase
Do not say "go ahead and implement everything".
Instead say:
- implement Phase 0 only,
- or implement Phase 1 only,
- etc.

### Step D — Require proof
After each implementation step, require:
- files changed,
- build result,
- tests run,
- summary of what is complete,
- summary of what is deferred,
- explicit mention of deviations from spec.

### Step E — Review before moving on
Before the next phase, check:
- is the build green?
- does the structure still match the spec?
- did the agent sneak in future-phase code?
- did it add Android capabilities you did not request?
- is the code still readable and modular?

### Step F — Re-anchor if needed
If the agent drifts:
- use the recovery prompt,
- force it to explain divergence,
- then resume from the correct phase.

## 4. The ideal flow by milestone

### Milestone 1 — Project skeleton
Use:
- Prompt 1
- Prompt 2

Your review questions:
- Does the project compile?
- Is Hilt wired correctly?
- Is the package structure sane?
- Did it avoid implementing random future code?

### Milestone 2 — Core engine foundation
Use:
- Prompt 3
- Prompt 4

Your review questions:
- Is the database model coherent?
- Is session state explicit?
- Is monotonic time used correctly?
- Did it avoid making UI the source of truth?

### Milestone 3 — User setup
Use:
- Prompt 5

Your review questions:
- Is onboarding understandable?
- Are permissions explained before request flows?
- Can blocked apps be selected and stored?

### Milestone 4 — Blocking system
Use:
- Prompt 6
- Prompt 7

Your review questions:
- Is AccessibilityService thin and delegated?
- Does the overlay path depend on a foreground Activity?
- Are duplicate overlays prevented?
- Is there an emergency exit?

### Milestone 5 — Automation
Use:
- Prompt 8
- Prompt 9

Your review questions:
- Is AlarmManager used only where exact timing is justified?
- Is WorkManager used for deferred work?
- Does calendar integration remain optional?

### Milestone 6 — Backup, polish, and hardening
Use:
- Prompt 10
- Prompt 11
- Prompt 12
- Prompt 13

Your review questions:
- Does backup stay optional?
- Are stats built on existing data instead of hacks?
- Are tests improving?
- Are the remaining risks documented honestly?

## 5. How to judge a good agent response

A good response usually:
- stays inside scope,
- names files before editing,
- explains design choices briefly,
- runs build or tests,
- admits limitations,
- does not silently invent platform capabilities.

A weak response usually:
- tries to do too much at once,
- handwaves Android restrictions,
- mixes UI, services, and domain logic tightly,
- avoids running builds,
- claims things are working without proof.

## 6. Red flags to stop immediately

Stop the agent and re-anchor if it:
- introduces device owner or kiosk logic,
- uses Device Admin as the foundation,
- claims it can prevent uninstall or force-stop reliably,
- moves scheduling, service, and UI logic into one class,
- makes accessibility callbacks contain all business logic,
- adds broad package visibility without a strong reason,
- adds Google dependencies to core functionality,
- implements multiple phases you did not ask for.

## 7. What to ask after every phase

Use this mini-checklist:

1. What files changed?
2. What exactly is now working?
3. What is still mocked, stubbed, or deferred?
4. What tests did you run?
5. What build command did you run?
6. What known risks remain?
7. What should the next phase be?

## 8. Practical human review strategy

Do not try to review every line equally.
Focus on:
- architecture boundaries,
- Android component usage,
- permissions,
- lifecycle behavior,
- persistence,
- recoverability,
- testability.

If those are correct, the smaller details are easier to fix later.

## 9. The safest cadence

A safe cadence is:
- one planning pass,
- one bootstrap pass,
- one implementation phase at a time,
- one audit every 2 to 3 phases,
- one hardening pass before release.

## 10. Suggested command rhythm for the agent

At a high level, every phase should follow this internal rhythm:

1. inspect current repo,
2. propose touched files,
3. implement,
4. compile,
5. run tests where relevant,
6. summarize,
7. stop.

## 11. If the repo becomes messy

If the project starts to feel inconsistent, do not keep piling features on top.
Instead:
1. run the architecture audit prompt,
2. ask for a refactor plan,
3. approve a limited cleanup scope,
4. require the build to stay green during cleanup.

## 12. Recommended bottom line

The best way to use Antigravity here is:
- give it strong written constraints,
- keep one source of truth,
- make it work phase by phase,
- require evidence after every step,
- keep control of the roadmap yourself.
