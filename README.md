Sample App
=======

Sample app has two features, one is a sample for database operations using with Android Alarm Manager
to create Different kinds of Reminders.

The other one is a small solution for an existing problem in Android.

Since ActivityGroup and TabActivity is deprecated I started to use Fragments in the Tab Host.

Problem : In the case you need to replace a Fragment for Fragment in any tab in Tab Host
You do not have a seperated stack for each tab under Tab Host.

Solution : I implemented a Custom Stack example that you can keep seperated stack for each tab under Tab Host

E.g

Tab 1 : Fragment A > Fragment B > Fragment C
Tab 2 : Fragment D > Fragment E

If you are under the Tab 1 and if you navigate through Fragment C from Fragment A
after that If you can the Tab to Tab 2 and switch back to Tab 1, you will see Fragment C as you left before you
changed the Tab. And if you press back button you will be navigated to Fragment B.


Twitter Plugin and Twitter Manager
=======

Twitter plugin for Cordova
This plugin allows you to share message on your twitter account.

Adding the Plugin to your project
Using this plugin requires Android Cordova.

To install the plugin, move twitter.js and twitter_controller.js (this is test case and only provides mock-up data) under www folder and include a reference to it in your html file after cordova.js.

<script type="text/javascript" charset="utf-8" src="cordova.js"></script>
<script type="text/javascript" charset="utf-8" src="twitter.js"></script>
<script type="text/javascript" charset="utf-8" src="twitter_controller.js"></script>

Create a directory within your project called "src/okan/apps/shareontwitter/" and move the .java files from this folder into it.

In your res/xml/plugins.xml file add the following line:

<plugin name="TwitterManager" value="okan.apps.shareontwitter.TwitterManager"/>

CAUTION: Using PhoneGap ≥ 2.0 (aka Cordova) you have to add this line into res/xml/config.xml in the <plugins>-section. The plugins.xml is no longer supported. The plugins are all located in the config.xml

Using the plugin

Take a look on twitter_controller.js to learn how to use this plugin.

Using the Manager

To use the manager you need to fill required fields in the Constants.java class.

Sample Application 2
=======

Sample Application 2 is mostly based on SQLite Database operations.

It has samples for ;

- Creating Tables with references to each other.
- General Search in whole Database regardless of Object type through predefined Collection.
- Easier Data Source control for saving and reading Objects from/to SQLite Database.

