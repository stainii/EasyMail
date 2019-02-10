# Building an app for my grand mother: barriers and solutions

One of my grandmothers has **difficulties hearing**.
Doing phone calls has become impossible, even with adapted phones that *yell* in your ear.

To avoid losing contact, we've tried to teach her how use send texts over sms.
This wasn't a great success: the keyboard confused her, there were too many steps, too many buttons: it went wrong easily.

Hell, I'm a developer. I surely can come up with an appropriate solution, right?
I've got an old Samsung Galaxy S5 lying around that my grand mother can use... so let's develop an "easy mail app"!

[*You can find the code on Github.*](https://github.com/stainii/EasyMail)

## Step 1: What's the easiest way to send and receive messages?

First and for all: what is the need?
* (must) **Read messages** and know **who has sent the message**
* (must) **Send messages**. 
* (nice to have) **Share photos** with my grand mother.

### The barriers of receiving a message
#### Lots of input at once
When you open an sms app, **the screen is filled** with previous messages with different persons. "What do I do now?", was the reaction.

Bombing someone with data, useful or not, creates strain. Too much strain, in case of my grand mother. 

### The barriers of sending a message
Sending sms messages was too hard. My grand mother got lost in the **keyboard**.

It contains **too many buttons**, which are, in addition, way **too small**.

Alternatively, we've tried to let her use the an "old keyboard", you know, like when phones were bricks called "Nokia". They have less keys, where you need to press once for "a", twice for "b", etc.
Less keys, but also too complicated.

### The barriers of using a smartphone
My grand mother never had a smartphone in her hand before. There are a lot of concepts that we find very natural, that she didn't grasp at first.

* **How to get back from where I came?**
	* On Android, the back button is **always on the same place**. That's helpful!
	* On my Galaxy S5 (and many other phones), **the back button is not a psychical button**. This means: **she does not recognize it as a button.**
	* **The home button, however, is a physical button and she has no problem pushing this button.**
  
![The back button is not a physical button. The home button is, however. So she pushes the latter.](https://stijnhooft.be/portfolio/data/blogposts/building-an-app-for-my-grand-mother/back-button.jpg)
	
* **Using a touch screen** is something that needs be learned.
	* My grand mother has long nails. Touching something with your nails doesn't work.
	* If she touches something, she presses the screen long and hard. This doesn't work either: the phone ignores hard and long presses.
	
* **What is a notification?**
	* We are used receiving, checking and reacting to notifications. My grand mother has never heard of it.
	 > "What, I need to swipe down to see new messages? What's swiping down?" 
	
  Jup, that's simply too complex...
  

### It should also be easy to use for the family members
Not everyone in my family is that handy with smartphones or computers. For them, receiving and sending messages should also be simple. If at all possible, without an extra app.

**Since my family members know how to use mail, I've chosen to make use of this.**

I've created a mail account for my grand mother. The app will act as a mail client.

* My family members can send messages to my grandmothers mail address, which will appear in my grand mother's app.
* When my grand mother sends messages to my mom, the app sends the message to my mother as an email.

One extra benefit: I don't need to run a backend on a server to save and provide the messages. Everything is done via mail.

![Reply, just like a regular mail](https://stijnhooft.be/portfolio/data/blogposts/building-an-app-for-my-grand-mother/reply-like-a-regular-mail.png)


## Step 2: Develop the most easy message screen possible

What does someone need to communicate?
* Whom am I talking to?
* What did the person say?
* A button to type a new message

With that in mind, I've created the following screen.

![Just people and messages](https://stijnhooft.be/portfolio/data/blogposts/building-an-app-for-my-grand-mother/just-people-and-messages.png)

**There are 5 things my grand mother needs to keep in mind**.
1. On the left, you have the photos of the people you can talk with. Tap the photo of the person who you want to talk to.

1. In the top border is written who you're talking with. "You're talking with Stijn".	

1. Your messages are in pink. The other person's messages are in green.

1. In order to send a message to the person you're talking with, hit the mail icon.

1. If you're stuck and want to restart, hit the home button.


## Step 3: Provide the least confusing keyboard

### Downloading a keyboard from the internet. What's in store?
One of the nice features of Android is the possibility to install another keyboard. I've searched for and tried several keyboard on the Google Play Store, but none really suited my needs.

The one that got the closest, was [1C Big Keyboard](https://play.google.com/store/apps/details?id=com.onecwearable.keyboard&hl=nl).

[![1C Big Keyboard](https://img.youtube.com/vi/P2EQXrgw4Jg/0.jpg)](https://www.youtube.com/watch?v=P2EQXrgw4Jg)

#### Problem 1: The keyboards take up too much space
Large buttons means: the keyboard takes up most space on the screen. My grand mother would no longer find the field in which she types.

#### Problem 2: The order of the buttons make no sense.
The design of a keyboard's layout takes several factors into account. The most important one: letters that are used frequently in combinations are put close to each other.

In Belgium, the most popular keyboard layout is azerty. Most of the world uses querty. 

**Azerty, querty, ... for my grand mother, the letters on a keyboard are just put in a random order**. She's scanning the whole keyboard for each character she wants to type, again and again. **This makes typing slow and frustrating.**


### Ok, then, let's try to build our own keyboard

What should the keyboard look like?

#### Which buttons do we need?
Buttons should be large enough. On the other hand, I don't want the keyboard to take up the whole screen.

A solution could be to leave out useless keys. Which buttons can we get rid of?

* **Absolutely needed**
	* a-z
	* space bar
	* backspace
	* point
	* question mark
* **Handy, but only if there is enough space left**
	* 0-9
	* exclamation mark
	* comma
* **Not needed at all**
	* special characters, like &, é, ", #, @
	* parentheses
	* other punctuation characters, like semicolon, colon, slash, ...
	* shift-lock
	* **shift?**

Did you ever got "screaming texts", all-caps, from non tech-savy people? **The concept of a shift button can be difficult!** I've decided to get rid of it. In this context, nobody cares about well-formed sentences.

#### In what order should the buttons be placed?
As I've mentioned earlier, an azerty or querty-layout makes no sense for my grandmother. "Why are all the letters put in a random order?"

**What keyboard layout would logical for her?** I've chosen to go with: ordering the **alphabetically**. Finding the position of a key still requires some thinking, but this process goes a whole lot faster than finding a character in a "random" tangle of keys. 

#### The concept of the "space bar" and "backspace"
When I first let my grand mother play with my "easy keyboard", I've noticed that she never used 2 buttons: space and backspace.

She just **kept typing, without leaving space between words**.
Did she make a spelling error? She just retyped the word, adding it to the original misspelled word.

> soyouendupwithsomthinglikethasthis

**When you have never touched a computer before, you don't know the concept of a space bar or backspace.**

In order to tackle this, I've **renamed the buttons. Space becomes "End of word" ("Einde woord" in Dutch). Backspace becomes "Erase" ("Wissen" in Dutch).**

![Send a message](https://stijnhooft.be/portfolio/data/blogposts/building-an-app-for-my-grand-mother/sending-a-message.png)



## Step 3: Always open the app on the "home" screen, never on the "send message" screen
As mentioned earlier, my grand mother does not find the back button on the phone. The back button may be always found at the same place, it is not a physical button. The home button is more accessible, since, yeah, it's a real **button**.

**In practice, my grand mother always presses the home button.**

That's not a problem. In the contrary: I've told her: "If you're stuck, just press that button. Then you can re-open the app and start over again".

However, in a default situation, that's not true. When you close an app, then reopen it, **the app will reopen the last activity**. This means: **she does not truly "start over"**. 

*Example:*
* She pushes the "new message" button by accident.
* The "new message" activity opens.  
* She pushes the home button, to go back
* She re-opens the app
* The "new message" reopens, since she left the app in that activity

[It takes a bit of trickery, but I've managed to let Android always open the home screen of the app, when the app is re-opened.](https://github.com/stainii/EasyMail/commit/2c048ddfd251b96d9c112d963a30ebe4826af7a5)

## Step 4: Finishing touches
### Change the notification sound to that my grandmother eventually hears
Since my grandmother's hearing is impaired, she doesn't hear a short notification.

I've replaced the notification sound with a [long song that she likes](https://www.youtube.com/watch?v=CmK-uaYFBJc]). There is a bigger chance that she hears this.

I'm looking for a better solution, though. Maybe something with light? If you have any ideas, let me know! :)
 

### Erase everything from the phone, leaving one button to press
* I've cleaned up the home screen, leaving only 1 app to open: the EasyMail app.
* Then, I've deleted all apps that could cause notifications.
* Turn up the notification volume to the max! :)
* Finally, I've turned the background in a nice, clear image. It's nice to look at and the EasyMail app is clearly visible.
![A very nice home screen. The app is clearly visible, ready to open.](https://stijnhooft.be/portfolio/data/blogposts/building-an-app-for-my-grand-mother/a-very-nice-home-screen.png)

### Disable screen rotation
Seeing that your screen changes completely is  **scary**  for non-tech-savvy people. Especially if it happens "for no reason", for example: when you tilt the phone slightly. That's why I  **have turned off screen rotation**  on the  phone.

### Turn off battery saving for this app
Android is very protective about the phone's battery life. When an app uses, relative to other apps, too much battery, it get's less CPU time.
Since, in my case, this app is the sole meaningful app on the phone, Android is a bit too eager to limit the mail checks in the app.

A workaround: turn off battery saving **for this app** (not for all apps per se).  [More info](https://www.verizonwireless.com/support/knowledge-base-202636/)

### Disable everything that uses battery
Using wifi? Turn off the mobile internet (or vice versa)

No, grandma does not need bluetooth, nor NFC or location services. 

## Result and final thoughts
![A very nice home screen. The app is clearly visible, ready to open.](https://stijnhooft.be/portfolio/data/blogposts/building-an-app-for-my-grand-mother/a-very-nice-home-screen.png)

![Just people and messages](https://stijnhooft.be/portfolio/data/blogposts/building-an-app-for-my-grand-mother/just-people-and-messages.png)

![Send a message](https://stijnhooft.be/portfolio/data/blogposts/building-an-app-for-my-grand-mother/sending-a-message.png)

![Reply, just like a regular mail](https://stijnhooft.be/portfolio/data/blogposts/building-an-app-for-my-grand-mother/reply-like-a-regular-mail.png)

### Using a smartphone is complicated!
Building an app for someone who has never touched a computer in her life, is quite challenging.

There are lots of concept that we find "normal", but are not normal in her world. To use the app, she needs to learn new concepts. Touching a screen. Opening an app. Typing messages, which is less intuitive than writing by hand "Space? backspace? Where do I find the letter U?".

For someone who is not used to them, a phone expresses tons of unexpected behaviour. "Ow, when I rotate the phone a bit, the whole screen changes! What?"

My goal was to limit the learning curve, to avoid scaring her away. This means: 
* disable everything unrelated, so that no weird popups occur
* make sure there are no more buttons on the screen than needed
* build a flow that is logical to her
* provide a simple way to restart, when she gets stuck.

#### Writing text messages... does not come naturally
Still, my grand mother has to learn a few things. One challenge that surprised me: **how to express her thoughts in a short message**.

Expressing your thoughts in a letter or phone call is a whole lot easier. She is used to write by hand. She has the space to write longs texts. Fixing writing mistakes is just crossing out words, or starting a new letter. In phone call, you get immediate response. There is room to explain the context you're in.

Sending texts may be second nature for most people, but it's not for someone who has never had to do it in her entire life.

### Will my grand mother's live improve with the app?
Time will tell if my grand mother **can** and **wants to** actually use the app. I really hope so.

It's hard to communicate with someone who cannot hear you properly. For us, as family, but most certainly for her. I hope this app gives her back the ability to have qualitative conversations with people. 
