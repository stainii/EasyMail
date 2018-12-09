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

TODO
-- Email address and developer key
-- Contacts and photos
-- Reply-to patterns to ignore

## Install the app
TODO

## Set the keyboard 
TODO

# Contributing to this project
I'm not an Android developer, and that's probably clear when you have a look at the code.
Feel free to comment in the issues, or create pull requests for improvements.

However, I'm not planning on doing feature requests.
Also, I feel that it's important to keep the app as simple as possible. There are no plans to add a configuration screen, for example, since I don't want my grandmother to get stuck in that screen by accidently pressing the wrong button.