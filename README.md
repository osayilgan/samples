samples
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
