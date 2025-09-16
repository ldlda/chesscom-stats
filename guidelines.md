# Guidelines

## workflow

1. submitting code

    Fork, open an issue (optional), then submit a PR, linking the issue if this PR fixes that issue.

2. commits

   - commits should be real small. Really fucking small. one commit does ONE thing only. a PR does a
     big thing which is multiple small things.
   - commit guidelines/help: [How to write a git commit](https://cbea.ms/git-commit/).
       - first sentence will be displayed. write in imperative mode, no trailing period

         e.g.: Fix builder methods of ExampleFragment

       - why did you write this code, add after a new line

         e.g.:
         ```
         Add support for...
         (blank line)
         i got /api/v1/users/{user} working lesgo
         ofc make this paragraph look better ChatGPT ts idfk
         ```

   - dont push to your remote right away. you have the chance to amend yo commit to either fix the
     commit msg or add an author

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

2. adding more as questions arrive.

## glhf
