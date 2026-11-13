# Portfolio Project Directions

Welcome to the portfolio project directions. Historically, this assignment
was completed in parts. But due to the excessive grading load that the
assignment created, the portfolio project was converted to a single submission.
The individual steps were maintained in this repo for your reference, but the
assignment must be completed as outlined in this document. In other words,
**ignore** any submission instructions you see in the individual docs.

## Assignment Description

In this assignment, you are tasked with creating a software component from
scratch using OSU's discipline as follows:

- Create the kernel and enhanced interfaces as described in the
   [component interfaces docs](03-component-interfaces/03-component-interfaces.md)
- Create the secondary abstract class as described in the
   [component abstract class docs](04-component-abstract-class/04-component-abstract-class.md)
- Create one kernel implementation as described in the
   [component kernel implementation docs](05-component-kernel-implementation/05-component-kernel-implementation.md)

In addition, you must complete the following tasks:

- Write a systematic test suite for all methods in each interface as well as the
  common methods as described in the
  [finishing touches doc](06-component-finishing-touches/06-component-finishing-touches.md)
- Write two qualitatively different use cases for your component as described in the
  [finishing touches doc](06-component-finishing-touches/06-component-finishing-touches.md)

Finally, you may take your component above and beyond expectations by
completing the following tasks:

- Create a CI pipeline that automates your testing on GitHub
- Generate documentation for you component using JavaDoc
- Version your component using your preferred versioning style

## Assignment Rubric

The assignment is graded broadly on the following categories. See Carmen for
the official rubric:

- **Discipline** (30%): how well does the component follow the discipline?
- **Testing** (25%): how systematic are the test cases?
- **Best Practices** (20%): how well-written is the code?
- **Use Cases** (15%): how well do the use cases show off the component?
- **Reflection** (10%): how thoughtful is the writing?

## Assignment Instructions

To complete the assignment, you must do the following **in order**:

1. Review this entire document from top-to-bottom
   - Make sure you have read the rubric and know how you're being assessed
2. Use this repo as a template, as described in the main [README](../README.md)
3. Clone this repo to your machine
4. Create a new branch from `main` and complete all of your work on it
   - `main` must only contain the template files at the time of submission
   - The new branch may be called whatever you like, but `portfolio-project` is
     fine
5. Create your component on your new branch
   - Commit early and often
   - Use descriptive commit messages (e.g., "Add enhanced interfaces")
6. Test your component and include two use cases showing it off
7. Complete the reflection in the next section
8. Create a pull request from your branch to `main`
9. Submit a link of the pull request to Carmen

## Assignment Reflection

Following the completion of the portfolio project, please take some time to
fill out the following reflection prompts. There is no word count, but at least
a few sentences per question would be ideal.

> A common gripe that students express is how much they feel the work they do
> in class fails to map to the real world. Now that you've had a chance to
> complete the portfolio project, how much better (or worse) do you think you
> understand software development and why?

This project was really good for me because it helped me to better understand the software development process. The project required us to develop an interface for a polynomial component, which is different from most of the other
assignments because in those we are given the interface to work with. It helped me understand what operations the kernel should provide, and how other methods can be built on top of them. It also showed me how one API decision affects the rest of the software.

> Also, did the portfolio project surface any gaps in your own knowledge of
> software development. If so, what are those gaps and how did you address them?

At first I wasn't sure how to decide whether a method should be in the
kernel of a class. For the polynomial component, I ran through the steps of
reading a single coefficient in the remove-and-restore version of the
component. To read the coefficient of a term, you have to remove several terms
from the polynomial, read the value of the coefficient, and then restore those
terms back to the polynomial. Since I expected to read coefficients a lot, I
put `coefficient(exponent)` in the kernel of the polynomial component.

> Finally, as a part of completing the portfolio project, to what extent has
> your perspective of software development changed, if at all? In other words,
> is software development something you still enjoy? If not, why not?

I still enjoy software development. The project gave me a different
perspective on the use of a polynomial component. I had anticipated that a
polynomial component would be useful for finding roots of functions. However,
the dice example surprised me because it modeled the probability of getting
each total when rolling two dice. It would be interesting to explore other
game mechanics that could be modeled with math.

> One of the challenges of completing the portfolio project is picking up a lot
> of skills on your own. Some of these skills are, of course, software skills.
> However, there are plenty of other skills you may have picked up through
> this process. Therefore, the first question is what skills did you pick up
> through this process?

I had to compare several different designs for the same functionality and
explain why I chose one over another. In addition, I had to be able to break
down a design decision into several individual components and compare each of
those components as well.

> The follow-up question is: could you rephrase these skills you picked up
> as bullet points that you could put on a resume? Try it below.

- Compared three Java API designs for a polynomial component and chose
  direct coefficient lookup to simplify access to individual terms.
- Explored two applications of a Java polynomial component through examples
  that calculate dice probability distributions and find polynomial roots and
  extrema.

> Next, how has working on this project affected your career trajectory?
> In other words, do you now hate the topic you picked? Or, are you even more
> interested in it? Both outcomes are valuable to your personal development.

I'm still interested in software engineering and AI, and this assignment
furthered my interest in the topic of optimization. In the root-finding
example, the derivative can describe the increase, decrease, or local
maximum/minimum of a function. I am interested in learning more about how this
is used in the context of machine learning, specifically in the training of a
model.

> Finally, consider the skills you've picked up and your current career
> trajectory. What are some things you could do to continue on your
> career trajectory? Also, who are some mentors you could contact to help
> you stay on your path?

Next, I would like to try implementing gradient descent for a simple
function. Then I would like to test the program with different learning rates
to see how the function behaves when the learning rate is too large. I would
like to ask my software course TA to look over the way I test the program to
make sure I am doing it correctly. I would also like to ask an upper-level
student who has taken a machine learning course what small project helped
them understand optimization.

## Assignment Advice

Broadly, this assignment may take anywhere from a few hours to over a full work
week (i.e., 40 hours) to complete depending on the complexity of your design.
Historically, the first part of this assignment was meant to help you scope down
your design, so it is still recommended to complete it even though it is not
graded. You may want to share your ideas with your instructor before you start
coding. In any case, it is your job to budget your time effectively.

With that said, it may not be a good idea to start early. Much of the
discipline will not be exposed to you until the end of week 3. Perhaps a good
starting time would be after your first midterm when you'll know if you need
an exam replacement.

In addition, you will have to learn some of the concepts (e.g., the common
methods) on your own, even though the course will cover them near the end of
the course. So, once you understand how to layer a component (i.e., after week
3), you can begin your work. Do not wait until the end of the course to start.

Also, you may submit the entire component **early** for a chance to get
feedback. There will be an early submission window, about a week before the
second midterm, where you can submit your completed component for an initial
grade. Any submissions after this point will not have a chance to resubmit.

Finally, if you have no idea where to start, consider checking out the
[part 1 docs](01-component-brainstorming/01-component-brainstorming.md).
There, you'll find a link to an article that includes project ideas. It is
recommended to follow the instructions in parts 1 through 6 anyway (even the
proof of concept for your own sanity), but you can continue to ignore the
individual submission instructions since you'll just be submitting everything at
once.
