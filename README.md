# EasyMail

One of my grandmothers has difficulties hearing.
Doing phone calls has become impossible, even with adapted phones.

To avoid losing contact, we've tried to teach her how use send texts over sms.
This wasn't a great success: the keyboard confused her, there were too many steps, too many buttons: it went wrong easily.

## An easy way to send mails
This project is an attempt to build the most easy text application possible.

My grandmother needs to do 4 steps, to send a message:
1. Open the app. It's the only app on the home screen.
1. Tap the photo of the person who you want to text.
1. Write your message on the simplest keyboard possible.
1. Hit send.

The message arrives an an email.
You can read and respond with your favorite email client.

### Why email?
I've chosen to use mail as a backend, instead of sms.
Although this requires her to have access to internet, this opens the possibility to send images to my grandmother.

# How to use this app yourself?

## Create a Gmail account
TODO

## Create a developer key
TODO

## Personalise the settings
Provide your own settings file as `app/src/main/assets/easymail.json`.
You can use `app/src/main/assets/easymail.example.json` as inspiration.

### Email address, subscription and sender
Configure the **email address** that will be used in EasyMail to send and receive messages.

Receiving messages is done by the **subscription**. Configure here 
* the IMAP settings of the mail provider.
* Also, you can indicate how often should be checked on mail.
    * If you make this interval smaller:
        * new messages come in faster, allowing faster interactions
        * more data is used
        * the battery will drain faster

Sending messages is done by the **sender**.
Configure here the SMTP settings of your mail provider.


### Contacts and photos
The app only shows messages from configured contacts.
In the *contacts* section of the settings file, provide for everyone that should be able to communicate with the user:
* name
* email address
* image name of every contact that should be able communicate with the user.
    * this image should be put in `/app/src/main/res/drawable`.
    * in the settings file, the file extension of the image may **not** be provided. If your file is called `stijn_thumb.jpg`, configure `stijn_thumb`.

### Response patterns
When replying, mail clients often add the original mail to the bottom of the reply:
> "On 20th of September, Stijn Hooft <stijnhooft@example.com> wrote: ".

The technical contract of mail is old and simple. There is no option to indicate "here is the start of the previous mail". It's the duty of the app to discover where the new message ends and the thread of previous messages start.

**When I send a message to my grand mother, she cannot see all the previous mails in the same text balloon. That's just confusing for her...
How to strip away the old mail thread? How to discover when a message ends and a thread of old messages begin?**

This application executes a a number of regexes on each line of the message. When the regex is met, the mail client assumes that the line is the first line of the thread of previous messages.
That line, and every line following, will not be shown to the user.

These regexes are defined in the settings property: "responsePatterns". This is an array of strings.

Sadly, there are is no agreement on how it should be indicated that the message ends and the thread of old messages start.
Have a test with the people who want to send mails to the user, with all the mail clients they use (computer, phone, browser).
Check how these clients start an old message and determine the regexes you need to add.

## Install the app
TODO

## Set the keyboard 
TODO

# Contributing to this project
I'm not an Android developer, and that's probably clear when you have a look at the code.
Feel free to comment in the issues, or create pull requests for improvements.

However, I'm not planning on doing feature requests.
Also, I feel that it's important to keep the app as simple as possible. There are no plans to add a configuration screen, for example, since I don't want my grandmother to get stuck in that screen by accidently pressing the wrong button.