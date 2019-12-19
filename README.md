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

This creates limitations, of course:
* The user needs to have access to the internet.
* Since email does not support push notifications (at least not without having a server at your disposal), there is a periodical pull for new mails. It's not possible to chat real-time.
    * When the app screen is open and visible, the check runs about every 30 seconds (give or take, Android can decide to do it a little earlier or later).
    * When the app is hidden/shut down, the check runs every 15 minutes (at most, Android can decide to delay the check to save battery)

# How to use this app yourself?

Note: this app has only been tested on a Samsung Galaxy S5 (API 23) until August 2019.
Since August 2019, the app is tested on a Samsung Galaxy A10 (API 28).

The latter is the only supported phone and API level.
It's possible that this app does not work on other phones or other API levels.

## 1. Create/use a mail account
I've created a GMail account for my grandmother.

### 1.1. GMail specific: activate SMTP
GMail does not allow mails to be fetched with SMTP by default. You can [activate](https://www.lifewire.com/how-to-enable-gmail-via-imap-1170856) this.

### 1.2. GMail specific: activate 2-step authentication and generate an app password
When testing this app, I've always used my own account. When I created a GMail account for my grandmother, I could not log in with the app. Google's security policy preevented this.
In order to be able to log in eventually, I've
1. [activated 2-step authentication](https://myaccount.google.com/u/1/signinoptions/two-step-verification?utm_source=google-account)
1. [generated an app password](https://myaccount.google.com/u/1/apppasswords?utm_source=google-account)

This generated app password needs to be filled in password in `settings.json` (see next step).

## 2. Personalise the settings
Provide your own settings file as `app/src/main/assets/easymail.json`.
You can use `app/src/main/assets/easymail.example.json` as inspiration.

### 2.1. Email address, receiver and sender
Configure the **email address** that will be used in EasyMail to send and receive messages.

Receiving messages is done by the **receiver**. Configure here 
* the IMAP settings of the mail provider.
* Also, you can indicate how often should be checked on mail.
    * If you make this interval smaller:
        * new messages come in faster, allowing faster interactions
        * more data is used
        * the battery will drain faster

Sending messages is done by the **sender**.
Configure here the SMTP settings of your mail provider.


### 2.2. Contacts and photos
The app only shows messages from configured contacts.
In the *contacts* section of the settings file, provide for everyone that should be able to communicate with the user:
* name
* email address
* image name of every contact that should be able communicate with the user.
    * this image should be put in `/app/src/main/res/drawable`.
    * in the settings file, the file extension of the image may **not** be provided. If your file is called `stijn_thumb.jpg`, configure `stijn_thumb`.

### 2.3. Response patterns
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

### 2.4. Notifications
Notifications can trigger
* sound
* a bluetooth signal

You can turn these features on or off with the "enabled" flag.

#### 2.4.1 Bluetooth
My grandmother has trouble hearing, and doesn't always notice the sound of the phone.
Therefore, by combining an Arduino, a HC-05 bluetooth module and a little LED strip, I've created a lamp that will shine when she has an unread message.

When receiving a message, the app turns on the phone's bluetooth and sends "1" to a bluetooth device with the name "HC-05".

If you want to use or build your own device, you can change the parameters (device name and the signal that the device listens to to turn on and off) in the settings file. 
 

## 3. Translate
All text, visible in the app, is set in `/app/src/main/res/values/strings.xml`.

Since my native language is Dutch, the provided text is in Dutch. The names of the keys are in English, however, so it should be easy to translate the app in your native language.


## 4. Build and install the app
Build and install this app with Android Studio.

## 5. Set the keyboard
Keyboards are weird: the letters are put in the wrong order and the buttons are way too small. At least, that's how non-tech savvy people see it.
In order to simplify this, the app comes with the most simple keyboard ever:
* letters ordered alphabetically
* numbers
* "end of word" (also known as space)
* erase

No shift, no emoji's, no things that can leave my grandmother in a place she gets confused.

In order to use this keyboard, you have to [change your phone settings](https://www.androidcentral.com/how-set-default-keyboard-your-android-phone).


## 6. Disable screen rotation
Seeing that your screen changes completely is **scary** for non-tech-savvy people. Especially if it happens "for no reason", for example: when you tilt the phone slightly.
That's why I **have turned off screen rotation** on my grandmother's phone.

**At the moment of writing, there is nothing in place to support screen rotation in the app.**

## 7. Turn off battery saving for this app
Android is very protective about the phone's battery life. When an app uses, relative to other apps, too much battery, it get's less CPU time.
Since, in my case, this app is the sole meaningful app on the phone, Android is a bit too eager to limit the mail checks in the app.

A workaround: turn off battery saving **for this app** (not for all apps per se).  [More info](https://www.verizonwireless.com/support/knowledge-base-202636/)

# Contributing to this project
I'm not an Android developer, and that's probably clear when you have a look at the code.
Feel free to comment in the issues, or create pull requests for improvements.

However, I'm not planning on doing feature requests.
Also, I feel that it's important to keep the app as simple as possible. There are no plans to add a configuration screen, for example, since I don't want my grandmother to get stuck in that screen by accidently pressing the wrong button.
