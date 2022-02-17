<h1 align="center">
  <br>
  <a href="https://twelveians.netrakc.com.np/">
    <img src="https://github.com/theGBguy/twelveians/blob/main/app/src/main/ic_launcher-playstore.png" alt="Twelveians" width="200"></a>
  <br>
  Twelveians
  <br>
</h1>

<!--- These are examples. See https://shields.io for others or to customize this set of shields. You might want to include dependencies, project status and licence info here --->
![GitHub issues](https://img.shields.io/github/issues/theGBguy/twelveians)
![GitHub forks](https://img.shields.io/github/forks/theGBguy/twelveians)
![GitHub stars](https://img.shields.io/github/stars/theGBguy/twelveians)
![GitHub license](https://img.shields.io/github/license/theGBguy/twelveians)
![Twitter Follow](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Ftwitter.com%2Fcgb_pandey)

An android application demo targetted to photo bloggers to upload photo as their blog content in a swift manner. 
Uses Imgur to store images anonymously and Google's Blogger to upload the blog posts which usually contains images.
Originally made for [Twelveians](https://twelveians.netrakc.com.np/) to let the admin post his content quickly.

## Prerequisites to use this project

Before you begin, ensure you have met the following requirements:
<!--- These are just example requirements. Add, duplicate or remove as required --->
* Register your application in the Imgur API console to get CLIENT-ID using this [link](https://api.imgur.com/oauth2/addclient).
* Create a new Google Cloud Console project and enable Blogger v3 api using this [link](https://console.cloud.google.com/).
* Create a Oauth2 client in the Google Cloud Console which authorizes permission to manage blogger account.
* Find out the BLOG-ID by signing in the blogger account using this [link](https://accounts.google.com/Login?service=blogger).

## Credential configuration

Create **"secrets.properties"** file in the root project directory in the following format: 
<pre><code>
// Blog id of the blog to communicate with blogger api.
BLOG_ID="YOUR_BLOGGER_BLOG_ID_HERE"

// Client id of the imgur to communicate with imgur endpoints.
CLIENT_ID="YOUR_IMGUR_CLIENT_ID_HERE"
</pre></code>

These keys will be used to communicate with Imgur and Blogger endpoints.

## Technology used
* Jetpack Compose
* Imgur API
* Blogger API

## Author

[theGBguy](https://github.com/theGBguy)

If you like this project, please star this project to encourage the author and fork it in case you need additional features.

## Contact

If you want to contact me you can reach me at <chiran604@gmail.com>.

## License
<!--- If you're not sure which open license to use see https://choosealicense.com/--->

This project uses the following license: [GNU GPL v3](<https://github.com/theGBguy/twelveians/blob/main/LICENSE>).
