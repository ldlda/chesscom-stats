# Guidelines

## workflow

1. submitting code via PR

   Fork, open an issue (optional), then submit a PR, linking the issue if this PR fixes that issue.

   When i ask questions about your PR better have answers my cuh

   After merge you can safely delete your branch (doesnt need to tho if weon care)

2. commits

    - commits should be real small. Really fucking small. one commit does ONE thing only. a PR does
      a big thing which is multiple small things.
    - commit guidelines/help: [How to write a git commit](https://cbea.ms/git-commit/).

        - first sentence will be displayed. write in imperative mode, no trailing period

          e.g.: [`Add Java API for Player and PlayerStats`][1]

        - why did you write this code, add after a new line

          e.g.:

          ```text
          Add support for...
          (blank line)
          i got /api/v1/users/{user} working lesgo
          ofc make this paragraph look better ChatGPT ts idfk
          ```

        - You can add topic in front idk like this:

          `docs: update guidelines`

        - Or you can follow [Conventional Commits](https://www.conventionalcommits.org/)

      be flexible. Be water, my friend.

    - dont push to your remote right away. you have the chance to amend yo commit to either fix the
      commit msg or add an author

   [1]: 9fb5066

3. Issues

   dont DM me. post an issue big dog

4. `.github/workflows`

   dont do that for now. ima add templates actually

## common help

1. we all hate android studio

   PLEASE check the following before committing:

    - an item that should be `gitignore`d appears in `git status`

      Unversioned Files my ass.

        - Absolutely dont commit build/ please thank you

   Please format your code before committing. Remember you can amend but local only.

2. PR conflicts:

    - if your work is still local, you can do whatever the fuck you want.

   rebase is your best bet. all your new work on top of remote. very cool.

    - if your work is public, oh well. you will need to solve some merge problems.

3. Please check the state of your code before PR. dont pr broken code.

   we know we write bad code. but dont PR broken code. infact dont commit broken code; but if you
   did, fix or revert it.

## glhf
