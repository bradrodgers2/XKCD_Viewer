package com.bradrodgers.xkcdviewer.testData

import com.bradrodgers.xkcdviewer.domain.ComicInfo

class TestData {
    companion object {
        val comicDummyData = ComicInfo(
            month = 4,
            num = 2297,
            link = "",
            year = 2020,
            news = "",
            safe_title = "Use or Discard By",
            transcript = "",
            alt = "One of the things of bear spray says that, and I'm not one to disobey safety instructions, but there are no bears around here. Guess it's time for a camping trip where we leave lots of food out!",
            img = "https://imgs.xkcd.com/comics/use_or_discard_by.png",
            title = "Use or Discard By",
            day = 22
        )
        val comicDummyData2 = ComicInfo(
            month = 8,
            num = 300,
            link = "",
            year = 2007,
            news = "",
            safe_title = "Facebook",
            transcript = "{{Title: Mildly sleazy uses of Facebook, part 14:}}\n{{subheading: Looking up someone's profile before introducing yourself so you know which of your favorite bands to mention}}\nBoy: Favorite bands? Hmm...\nBoy: Maybe Regina Spektor or the Polyphonic Spree.\nGirl: Whoa, those are two of my favorites, too!\nGirl: Clearly, we should have sex.\nBoy: Okay!  My favorite position is the retrograde wheelbarrow.\nGirl: [[arms in the air]] Ohmygod, mine too!\n{{alt-text: 'Here, I'll put my number in your cell pho -- wait, why is it already here?'}}",
            alt = "'Here, I'll put my number in your cell pho -- wait, why is it already here?'",
            img = "https://imgs.xkcd.com/comics/facebook.png",
            title = "Facebook",
            day = 8
        )
    }
}
