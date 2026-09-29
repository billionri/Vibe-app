package com.example.data

import com.example.model.Coupon
import com.example.model.MerchProduct
import com.example.model.ReelItem
import com.example.model.StoreMovie
import com.example.model.TrackItem
import com.example.model.VinylRecord

object VibeDataDefaults {

    val DEFAULT_TRACKS = listOf(
        TrackItem(
            id = "rkWJyMhIWLo",
            title = "Starboy - The Weeknd ft. Daft Punk",
            channel = "The Weeknd",
            labels = listOf("Night", "Hype")
        ),
        TrackItem(
            id = "DCFrCX4HPO8",
            title = "After Dark - Mr.Kitty",
            channel = "Mr.Kitty",
            labels = listOf("Night", "Chill")
        ),
        TrackItem(
            id = "J_d_Q3pTYcc",
            title = "Sweater Weather - The Neighbourhood",
            channel = "The Neighbourhood",
            labels = listOf("Romantic", "Chill")
        ),
        TrackItem(
            id = "H7Y5a7Y-jng",
            title = "Resonance - HOME (Synthwave)",
            channel = "ChilledCow",
            labels = listOf("Chill", "Night")
        ),
        TrackItem(
            id = "Mod_oXpftJA",
            title = "Thamma Official Soundtrack - Sachin-Jigar",
            channel = "Maddock Films",
            labels = listOf("Party", "Hype")
        ),
        TrackItem(
            id = "GTOdXVfrXF0",
            title = "Toota Jo Kabhi Tara - Sumedha K | Sachin Jigar",
            channel = "Zee Music Company",
            labels = listOf("Romantic", "Sad")
        ),
        TrackItem(
            id = "OiBo_NgYI5Q",
            title = "Gilded Lily - Cults (Speed Up Lo-Fi)",
            channel = "VIBE Sounds",
            labels = listOf("Chill", "Sad")
        )
    )

    val STORE_MOVIES = listOf(
        // MARVEL
        StoreMovie(id = "eOrNdBpGMv8", title = "Avengers: Endgame", year = "2019", genre = "Action", collection = "Marvel", emoji = "⚡", rating = "8.4", toy = "Iron Man Figure", isWatched = true),
        StoreMovie(id = "TcMBFSGVi1c", title = "Iron Man", year = "2008", genre = "Action", collection = "Marvel", emoji = "🤖", rating = "7.9", toy = "Iron Man Keychain", isWatched = true),
        StoreMovie(id = "hA6hldpSTF8", title = "Spider-Man: No Way Home", year = "2021", genre = "Action", collection = "Marvel", emoji = "🕷️", rating = "8.3", toy = "Spider-Man Figure", isWatched = true),
        StoreMovie(id = "d9MyW72ELq0", title = "Black Panther", year = "2018", genre = "Action", collection = "Marvel", emoji = "🐾", rating = "7.3", toy = "Black Panther Sticker"),
        StoreMovie(id = "go6GEIrcvFY", title = "Doctor Strange", year = "2016", genre = "Fantasy", collection = "Marvel", emoji = "🔮", rating = "7.5", toy = "Marvel Poster"),
        StoreMovie(id = "6ZfuNTut_oQ", title = "Thor: Ragnarok", year = "2017", genre = "Action", collection = "Marvel", emoji = "⚡", rating = "7.9", toy = "Thor Hammer Mini"),

        // DC
        StoreMovie(id = "n2SS_HBXZSY", title = "The Dark Knight", year = "2008", genre = "Action", collection = "DC", emoji = "🦇", rating = "9.0", toy = "Batman Keychain", isWatched = true),
        StoreMovie(id = "wg5ZBNs7sXY", title = "Wonder Woman", year = "2017", genre = "Action", collection = "DC", emoji = "🦅", rating = "7.4", toy = "WW Wristband"),
        StoreMovie(id = "0WWzgGyAH6Y", title = "Aquaman", year = "2018", genre = "Action", collection = "DC", emoji = "🌊", rating = "6.9", toy = "DC Sticker Pack"),
        StoreMovie(id = "qaW3YsGbKTI", title = "Joker", year = "2019", genre = "Drama", collection = "DC", emoji = "🃏", rating = "8.4", toy = "Joker Poster Print", isWatched = true),
        StoreMovie(id = "T6DJcgm3wNY", title = "Superman: Man of Steel", year = "2013", genre = "Action", collection = "DC", emoji = "🦸", rating = "7.0", toy = "Superman Cape Mini"),

        // ANIME
        StoreMovie(id = "ByXuk9QqQkk", title = "Demon Slayer: Mugen Train", year = "2020", genre = "Anime", collection = "Anime", emoji = "⚔️", rating = "8.3", toy = "Demon Slayer Sticker", isWatched = true),
        StoreMovie(id = "VbABzxkfBHk", title = "Your Name (Kimi no Na wa)", year = "2016", genre = "Anime", collection = "Anime", emoji = "🌠", rating = "8.4", toy = "Anime Art Print", isWatched = true),
        StoreMovie(id = "4cHmUdSQBdI", title = "Spirited Away", year = "2001", genre = "Anime", collection = "Anime", emoji = "🐉", rating = "8.6", toy = "Ghibli Sticker Pack", isWatched = true),
        StoreMovie(id = "oFaAssTDGgQ", title = "Attack on Titan: The Movie", year = "2022", genre = "Anime", collection = "Anime", emoji = "⚔️", rating = "8.5", toy = "AOT Badge"),
        StoreMovie(id = "hf22K7BCCW0", title = "Naruto: The Last", year = "2014", genre = "Anime", collection = "Anime", emoji = "🍥", rating = "7.6", toy = "Naruto Headband Mini"),

        // BOLLYWOOD
        StoreMovie(id = "bxFqSCnuGSM", title = "Pathaan", year = "2023", genre = "Action", collection = "Bollywood", emoji = "🔥", rating = "5.9", toy = "Movie Poster"),
        StoreMovie(id = "v4VMcMjLYbQ", title = "Jawan", year = "2023", genre = "Action", collection = "Bollywood", emoji = "💥", rating = "6.6", toy = "SRK Sticker"),
        StoreMovie(id = "WGwE7KCFB_A", title = "Brahmastra", year = "2022", genre = "Fantasy", collection = "Bollywood", emoji = "🔱", rating = "5.6", toy = "Astro Poster"),
        StoreMovie(id = "4EyH4l0dRJE", title = "RRR", year = "2022", genre = "Action", collection = "Bollywood", emoji = "🌊", rating = "7.8", toy = "RRR Badge", isWatched = true),
        StoreMovie(id = "MpFk_LNxgDk", title = "KGF Chapter 2", year = "2022", genre = "Action", collection = "Bollywood", emoji = "⛏️", rating = "8.2", toy = "KGF Keychain"),

        // HOLLYWOOD
        StoreMovie(id = "sY1S34973zA", title = "Inception", year = "2010", genre = "Sci-Fi", collection = "Hollywood", emoji = "🌀", rating = "8.8", toy = "Movie Art Print", isWatched = true),
        StoreMovie(id = "eTn_J9BgFXk", title = "Interstellar", year = "2014", genre = "Sci-Fi", collection = "Hollywood", emoji = "🌌", rating = "8.7", toy = "Space Sticker Pack", isWatched = true),
        StoreMovie(id = "egD56TOzNKM", title = "Avatar: The Way of Water", year = "2022", genre = "Sci-Fi", collection = "Hollywood", emoji = "💧", rating = "7.6", toy = "Avatar Poster"),
        StoreMovie(id = "5PSNL1qE6VY", title = "Top Gun: Maverick", year = "2022", genre = "Action", collection = "Hollywood", emoji = "✈️", rating = "8.3", toy = "Top Gun Badge")
    )

    val SHOP_PRODUCTS = listOf(
        MerchProduct(
            id = "sp1",
            name = "UBON 10W Bluetooth Speaker",
            desc = "Where Style Meets Sound · Deep Bass · Wireless · Carry Strap · Perfect for parties & travel",
            price = 450,
            originalPrice = 999,
            emoji = "🔊",
            category = "audio",
            badge = "HOT DEAL 55% OFF"
        ),
        MerchProduct(
            id = "sp2",
            name = "VIBE Quote Oversized Tee",
            desc = "\"Money follows maah brothaaa\" · Black premium cotton · Unisex fit · Sizes S to XXL",
            price = 500,
            originalPrice = 999,
            emoji = "👕",
            category = "clothing",
            badge = "LIMITED MERCH"
        ),
        MerchProduct(
            id = "sp3",
            name = "Tecpods TWS Earbuds Pro",
            desc = "13mm Driver · 30H Battery · BT 5.4 · Water Resistant · HD Mic · Ergonomic fit",
            price = 350,
            originalPrice = 1299,
            emoji = "🎧",
            category = "audio",
            badge = "BESTSELLER"
        ),
        MerchProduct(
            id = "sp4",
            name = "VIBE Embroidered Cap",
            desc = "Stylish embroidered VIBE logo cap · Adjustable strap · One size fits all",
            price = 199,
            originalPrice = 499,
            emoji = "🧢",
            category = "clothing"
        ),
        MerchProduct(
            id = "sp6",
            name = "VIBE Resistance Band Set",
            desc = "Set of 5 workout bands · Light to heavy resistance · Full body fitness · Carry bag included",
            price = 249,
            originalPrice = 699,
            emoji = "💪",
            category = "fitness"
        ),
        MerchProduct(
            id = "sp7",
            name = "VIBE 360° Phone Stand",
            desc = "Adjustable aluminium desk phone stand · 360° rotation · Foldable · Universal fit",
            price = 99,
            originalPrice = 249,
            emoji = "📱",
            category = "accessories"
        ),
        MerchProduct(
            id = "sp8",
            name = "VIBE Aesthetic Sticker Pack",
            desc = "10 premium vinyl stickers · Waterproof · For phones, laptops, guitars & helmets",
            price = 49,
            originalPrice = 99,
            emoji = "🎨",
            category = "accessories"
        )
    )

    val VINYL_RECORDS = listOf(
        VinylRecord(id = "vr1", album = "Discovery", artist = "Daft Punk", year = "2001", price = 2400, emoji = "💿", genre = "French House / Electro"),
        VinylRecord(id = "vr2", album = "The Dark Side of the Moon", artist = "Pink Floyd", year = "1973", price = 3100, emoji = "🌈", genre = "Progressive Rock"),
        VinylRecord(id = "vr3", album = "IGOR", artist = "Tyler, The Creator", year = "2019", price = 2650, emoji = "💖", genre = "Neo-Soul / Hip-Hop"),
        VinylRecord(id = "vr4", album = "AM", artist = "Arctic Monkeys", year = "2013", price = 2200, emoji = "📻", genre = "Indie Rock"),
        VinylRecord(id = "vr5", album = "Blonde", artist = "Frank Ocean", year = "2016", price = 3500, emoji = "🌊", genre = "R&B / Soul"),
        VinylRecord(id = "vr6", album = "Currents", artist = "Tame Impala", year = "2015", price = 2800, emoji = "🌀", genre = "Psychedelic Pop")
    )

    val SAMPLE_REELS = listOf(
        ReelItem(id = "r1", title = "Late Night Rain in Shinjuku", creator = "@tokyodrifter", sound = "Resonance (Lo-Fi Slowed)", likes = "142K", emoji = "🌧️"),
        ReelItem(id = "r2", title = "Synthwave Cruise on Pacific Coast Highway", creator = "@neonvibes", sound = "Kavinsky - Nightcall", likes = "89K", emoji = "🏎️"),
        ReelItem(id = "r3", title = "Studio Ghibli aesthetic coffee morning", creator = "@cafekitchen", sound = "Ylang Ylang - FKJ", likes = "215K", emoji = "☕"),
        ReelItem(id = "r4", title = "Cyberpunk 2077 Night City Drone Shots", creator = "@bladecity", sound = "After Dark - Mr.Kitty", likes = "320K", emoji = "⚡")
    )

    val COUPONS = listOf(
        Coupon(code = "VIBE50", discount = "50% OFF", desc = "Applicable on all VIBE Merch orders above ₹400"),
        Coupon(code = "MOVIEBUFF", discount = "₹100 CASHBACK", desc = "Movie Theatre watch party snack pass voucher"),
        Coupon(code = "VINYLVIP", discount = "FREE SHIPPING", desc = "Free doorstep delivery on any vinyl record")
    )
}
