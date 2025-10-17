# what the fuck is this

when i type in the search bar in chesscom and press the mighty button ctrl shift i, what i see is a
post req to
`https://www.chess.com/service/friends-search/idl/chesscom.friends_search.v1.FriendsSearchService/Autocomplete`
in this shape:

```json
{
  "prefix": "magnuscarlsen",
  "includeFriends": true,
  "includeSuggestions": true,
  "friendsLimit": 6,
  "suggestionsLimit": 6,
  "exactUsernameFirst": true,
  "boostUsername": true
}
```

and a result might look like this:

```json
{
  "suggestions": [
    {
      "uuid": "6f4deb88-7718-11e3-8016-000000000000",
      "userView": {
        "id": "6f4deb88-7718-11e3-8016-000000000000",
        "userId": "15448422",
        "type": "USER_TYPE_USER",
        "username": "Hikaru",
        "firstName": "Hikaru Nakamura",
        "chessTitle": "GM",
        "avatar": {
          "url": "https://images.chesscomfiles.com/uploads/v1/user/15448422.88c010c1.50x50o.635d785c657a.png",
          "highResolutionUrl": "https://images.chesscomfiles.com/uploads/v1/user/15448422.88c010c1.200x200o.3c5619f5441e.png"
        },
        "membership": "diamond",
        "country": "US",
        "timezone": "America/New_York",
        "createdAt": "2014-01-06T21:20:58Z",
        "updatedAt": "2025-10-15T18:42:43.099Z",
        "isEnabled": true,
        "isContentHidden": false,
        "isStreamer": true,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "nothing"
      },
      "lastLoginDate": "2025-10-15T17:02:11.591Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_ONLINE",
      "bestRating": 3348,
      "country": {
        "id": 2,
        "name": "United States",
        "code": "US"
      },
      "ratings": [
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CRAZYHOUSE_BLITZ",
          "rating": 2278
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS960_BLITZ",
          "rating": 2866
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BLITZ",
          "rating": 3317
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_KINGOFTHEHILL_BLITZ",
          "rating": 2089
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_RAPID",
          "rating": 2839
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_BUGHOUSE_BLITZ",
          "rating": 2130
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_THREECHECK_BLITZ",
          "rating": 2291
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BULLET",
          "rating": 3348
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_DAILY",
          "rating": 2239
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS960_DAILY",
          "rating": 1231
        }
      ]
    },
    {
      "uuid": "009a9266-c376-11ea-9492-0d77bd55b8b1",
      "userView": {
        "id": "009a9266-c376-11ea-9492-0d77bd55b8b1",
        "userId": "86419862",
        "type": "USER_TYPE_USER",
        "username": "GMHikaruOnTwitch",
        "firstName": "Hikaru",
        "lastName": "Nakamura",
        "chessTitle": "GM",
        "avatar": {
          "url": "https://images.chesscomfiles.com/uploads/v1/user/86419862.bd239f8f.30x30o.6e47709bc1ac.png",
          "highResolutionUrl": "https://images.chesscomfiles.com/uploads/v1/user/86419862.bd239f8f.200x200o.8d80de7afe67.png"
        },
        "flair": {
          "id": "ac06975e-2af1-11ee-9eb7-e1f0276c12e2",
          "images": {
            "lottie": "https://images.chesscomfiles.com/chess-flair/hosts/hikaru.lottie",
            "svg": "https://images.chesscomfiles.com/chess-flair/hosts/hikaru.svg"
          }
        },
        "membership": "diamond",
        "country": "AT",
        "timezone": "America/Los_Angeles",
        "createdAt": "2020-07-11T12:56:57Z",
        "updatedAt": "2024-03-19T18:52:55.398Z",
        "isEnabled": true,
        "isContentHidden": true,
        "isStreamer": true,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "hikaru"
      },
      "lastLoginDate": "2025-08-13T18:57:06.778Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_OFFLINE",
      "bestRating": 3141,
      "country": {
        "id": 18,
        "name": "Austria",
        "code": "AT"
      },
      "ratings": [
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS960_BLITZ",
          "rating": 2895
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BLITZ",
          "rating": 2908
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_RAPID",
          "rating": 2627
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_THREECHECK_BLITZ",
          "rating": 2679
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BULLET",
          "rating": 3141
        }
      ]
    },
    {
      "uuid": "3e0579b0-ec26-11ec-a83d-37c430f088d8",
      "userView": {
        "id": "3e0579b0-ec26-11ec-a83d-37c430f088d8",
        "userId": "186904033",
        "type": "USER_TYPE_USER",
        "username": "hikarutv1",
        "avatar": {
          "url": "https://images.chesscomfiles.com/uploads/v1/user/186904033.3daf3940.50x50o.c285cfe1d4c5.png",
          "highResolutionUrl": "https://images.chesscomfiles.com/uploads/v1/user/186904033.3daf3940.200x200o.72a9dbbfbafd.png"
        },
        "flair": {
          "id": "abd554aa-2af1-11ee-bb50-1bb6daf0ee3e",
          "images": {
            "lottie": "https://images.chesscomfiles.com/chess-flair/membership_icons/diamond_traditional.lottie",
            "png": "https://images.chesscomfiles.com/chess-flair/membership_icons/diamond_traditional.png",
            "svg": "https://images.chesscomfiles.com/chess-flair/membership_icons/diamond_traditional.svg"
          }
        },
        "membership": "diamond",
        "country": "US",
        "timezone": "America/New_York",
        "createdAt": "2022-06-14T21:09:13Z",
        "updatedAt": "2025-10-15T18:31:39.007Z",
        "isEnabled": true,
        "isContentHidden": false,
        "isStreamer": true,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "diamond_traditional"
      },
      "lastLoginDate": "2025-10-15T11:13:27.051Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_ONLINE",
      "country": {
        "id": 2,
        "name": "United States",
        "code": "US"
      }
    },
    {
      "uuid": "347730d6-ce3f-11ed-bcac-c165fedf8c67",
      "userView": {
        "id": "347730d6-ce3f-11ed-bcac-c165fedf8c67",
        "userId": "268340555",
        "type": "USER_TYPE_USER",
        "username": "GMHikaruOnKick",
        "firstName": "Hikaru",
        "lastName": "Nakamura",
        "avatar": {
          "url": "https://images.chesscomfiles.com/uploads/v1/user/268340555.fc48a046.30x30o.38457c8592d5.png",
          "highResolutionUrl": "https://images.chesscomfiles.com/uploads/v1/user/268340555.fc48a046.200x200o.af0f090f6ac4.png"
        },
        "membership": "basic",
        "country": "US",
        "timezone": "America/Toronto",
        "createdAt": "2023-03-29T14:37:17Z",
        "updatedAt": "2025-03-31T14:39:19.892Z",
        "isEnabled": true,
        "isContentHidden": true,
        "isStreamer": true,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "nothing"
      },
      "lastLoginDate": "2023-07-26T06:34:16.109Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_OFFLINE",
      "bestRating": 2694,
      "country": {
        "id": 2,
        "name": "United States",
        "code": "US"
      },
      "ratings": [
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BLITZ",
          "rating": 2311
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BULLET",
          "rating": 2694
        }
      ]
    },
    {
      "uuid": "b2a2b4b8-ec26-11ec-be19-dd83b52cf5f3",
      "userView": {
        "id": "b2a2b4b8-ec26-11ec-be19-dd83b52cf5f3",
        "userId": "186904269",
        "type": "USER_TYPE_USER",
        "username": "hikarutv3",
        "avatar": {
          "url": "https://www.chess.com/bundles/web/images/noavatar_l.84a92436.gif",
          "highResolutionUrl": "https://www.chess.com/bundles/web/images/noavatar_l.84a92436.gif"
        },
        "membership": "basic",
        "country": "US",
        "timezone": "America/New_York",
        "createdAt": "2022-06-14T21:12:29Z",
        "updatedAt": "2023-12-09T17:35:37.731Z",
        "isEnabled": true,
        "isContentHidden": false,
        "isStreamer": true,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "nothing"
      },
      "lastLoginDate": "2025-02-14T22:37:32.710Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_OFFLINE",
      "country": {
        "id": 2,
        "name": "United States",
        "code": "US"
      }
    },
    {
      "uuid": "5e041438-ec26-11ec-937f-fffa776c62d4",
      "userView": {
        "id": "5e041438-ec26-11ec-937f-fffa776c62d4",
        "userId": "186904083",
        "type": "USER_TYPE_USER",
        "username": "hikarutv2",
        "avatar": {
          "url": "https://www.chess.com/bundles/web/images/noavatar_l.84a92436.gif",
          "highResolutionUrl": "https://www.chess.com/bundles/web/images/noavatar_l.84a92436.gif"
        },
        "membership": "basic",
        "country": "US",
        "timezone": "America/New_York",
        "createdAt": "2022-06-14T21:10:07Z",
        "updatedAt": "2023-12-12T16:26:13.143Z",
        "isEnabled": true,
        "isContentHidden": false,
        "isStreamer": true,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "nothing"
      },
      "lastLoginDate": "2024-11-29T16:59:59.420Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_OFFLINE",
      "country": {
        "id": 2,
        "name": "United States",
        "code": "US"
      }
    }
  ]
}
```

or this:

```json
{
  "suggestions": [
    {
      "uuid": "a2761738-b155-11df-8018-000000000000",
      "userView": {
        "id": "a2761738-b155-11df-8018-000000000000",
        "userId": "3889224",
        "type": "USER_TYPE_USER",
        "username": "MagnusCarlsen",
        "firstName": "Magnus",
        "lastName": "Carlsen",
        "chessTitle": "GM",
        "avatar": {
          "url": "https://images.chesscomfiles.com/uploads/v1/user/3889224.121e2094.50x50o.c9e2d3e54344.jpg",
          "highResolutionUrl": "https://images.chesscomfiles.com/uploads/v1/user/3889224.121e2094.200x200o.361c2f8a59c2.jpg"
        },
        "membership": "diamond",
        "country": "NO",
        "timezone": "Europe/Budapest",
        "createdAt": "2010-08-26T21:05:20Z",
        "updatedAt": "2025-10-15T18:43:37.425Z",
        "isEnabled": true,
        "isContentHidden": false,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "diamond_traditional"
      },
      "lastLoginDate": "2025-10-15T19:09:48.640Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_OFFLINE",
      "bestRating": 3333,
      "country": {
        "id": 104,
        "name": "Norway",
        "code": "NO"
      },
      "ratings": [
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS960_BLITZ",
          "rating": 2789
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BLITZ",
          "rating": 3333
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_RAPID",
          "rating": 2941
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_THREECHECK_BLITZ",
          "rating": 2619
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BULLET",
          "rating": 3150
        }
      ]
    },
    {
      "uuid": "b67d5a80-d0cf-11ea-b47b-e9cd70d21898",
      "userView": {
        "id": "b67d5a80-d0cf-11ea-b47b-e9cd70d21898",
        "userId": "87759694",
        "type": "USER_TYPE_USER",
        "username": "MagnusCarlsen33333",
        "firstName": "Hans",
        "lastName": "Ruedi",
        "avatar": {
          "url": "https://images.chesscomfiles.com/uploads/v1/user/87759694.9b009e33.50x50o.300ff80f96e3.jpeg",
          "highResolutionUrl": "https://images.chesscomfiles.com/uploads/v1/user/87759694.9b009e33.200x200o.e4cf3ceb38be.jpeg"
        },
        "membership": "basic",
        "country": "CH",
        "timezone": "America/Los_Angeles",
        "createdAt": "2020-07-28T12:41:52Z",
        "updatedAt": "2025-10-15T11:00:51.306Z",
        "isEnabled": true,
        "isContentHidden": false,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "nothing"
      },
      "lastLoginDate": "2025-10-15T11:22:46.636Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_OFFLINE",
      "bestRating": 1418,
      "country": {
        "id": 133,
        "name": "Switzerland",
        "code": "CH"
      },
      "ratings": [
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CRAZYHOUSE_BLITZ",
          "rating": 763
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS960_BLITZ",
          "rating": 748
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BLITZ",
          "rating": 1240
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_KINGOFTHEHILL_BLITZ",
          "rating": 623
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_RAPID",
          "rating": 1271
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_BUGHOUSE_BLITZ",
          "rating": 1019
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_THREECHECK_BLITZ",
          "rating": 818
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BULLET",
          "rating": 1082
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_DAILY",
          "rating": 1418
        }
      ]
    },
    {
      "uuid": "fd28f494-5fd7-11ef-ad39-e9539b016548",
      "userView": {
        "id": "fd28f494-5fd7-11ef-ad39-e9539b016548",
        "userId": "382403397",
        "type": "USER_TYPE_USER",
        "username": "MagnusCarlsen_55587",
        "firstName": "xuan phat",
        "avatar": {
          "url": "https://images.chesscomfiles.com/uploads/v1/user/382403397.38f6b966.50x50o.60394f5f3c60.jpg",
          "highResolutionUrl": "https://images.chesscomfiles.com/uploads/v1/user/382403397.38f6b966.200x200o.cf70fbac7c86.jpg"
        },
        "flair": {
          "id": "abdcfec6-2af1-11ee-a736-4deb875c5241",
          "images": {
            "lottie": "https://images.chesscomfiles.com/chess-flair/membership_icons/crown_rainbow.lottie",
            "png": "https://images.chesscomfiles.com/chess-flair/membership_icons/crown_rainbow.png",
            "svg": "https://images.chesscomfiles.com/chess-flair/membership_icons/crown_rainbow.svg"
          }
        },
        "membership": "platinum",
        "country": "VN",
        "timezone": "America/Edmonton",
        "createdAt": "2024-08-21T16:11:14Z",
        "updatedAt": "2025-10-14T22:52:36.344Z",
        "isEnabled": true,
        "isContentHidden": false,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "crown_rainbow"
      },
      "lastLoginDate": "2025-10-14T22:52:37.986Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_OFFLINE",
      "bestRating": 1245,
      "country": {
        "id": 149,
        "name": "Vietnam",
        "code": "VN"
      },
      "ratings": [
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CRAZYHOUSE_BLITZ",
          "rating": 827
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS960_BLITZ",
          "rating": 676
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BLITZ",
          "rating": 580
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_KINGOFTHEHILL_BLITZ",
          "rating": 797
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_RAPID",
          "rating": 771
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_BUGHOUSE_BLITZ",
          "rating": 682
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_THREECHECK_BLITZ",
          "rating": 847
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BULLET",
          "rating": 1245
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_DAILY",
          "rating": 720
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS960_DAILY",
          "rating": 400
        }
      ]
    },
    {
      "uuid": "c112e28a-dbd0-11ed-ba86-67eea46c39fc",
      "userView": {
        "id": "c112e28a-dbd0-11ed-ba86-67eea46c39fc",
        "userId": "275626891",
        "type": "USER_TYPE_USER",
        "username": "MagnusCarlsenclm",
        "firstName": "Magnus version 2",
        "lastName": "carlsen",
        "avatar": {
          "url": "https://www.chess.com/bundles/web/images/noavatar_l.84a92436.gif",
          "highResolutionUrl": "https://www.chess.com/bundles/web/images/noavatar_l.84a92436.gif"
        },
        "membership": "basic",
        "country": "JP",
        "timezone": "Asia/Tokyo",
        "createdAt": "2023-04-15T21:01:55Z",
        "updatedAt": "2024-03-19T18:57:48.395Z",
        "isEnabled": true,
        "isContentHidden": false,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "nothing"
      },
      "lastLoginDate": "2025-08-03T22:32:24.216Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_OFFLINE",
      "bestRating": 1106,
      "country": {
        "id": 78,
        "name": "Japan",
        "code": "JP"
      },
      "ratings": [
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CRAZYHOUSE_BLITZ",
          "rating": 571
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BLITZ",
          "rating": 591
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_KINGOFTHEHILL_BLITZ",
          "rating": 454
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_RAPID",
          "rating": 1106
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_THREECHECK_BLITZ",
          "rating": 537
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BULLET",
          "rating": 560
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_DAILY",
          "rating": 464
        }
      ]
    },
    {
      "uuid": "266dc2d2-3db7-11ea-9581-4b7e729e8a1a",
      "userView": {
        "id": "266dc2d2-3db7-11ea-9581-4b7e729e8a1a",
        "userId": "71155412",
        "type": "USER_TYPE_USER",
        "username": "MagnusCarlsenNAD",
        "firstName": "Magnus",
        "lastName": "Carlsen",
        "avatar": {
          "url": "https://images.chesscomfiles.com/uploads/v1/user/71155412.20c10b99.50x50o.8e6dd4d5fe63.jpg",
          "highResolutionUrl": "https://images.chesscomfiles.com/uploads/v1/user/71155412.20c10b99.200x200o.950bce10081a.jpg"
        },
        "flair": {
          "id": "abea4c8e-2af1-11ee-b4fd-855bd7a5284e",
          "images": {
            "lottie": "https://images.chesscomfiles.com/chess-flair/emoji/flag_ukraine.lottie",
            "svg": "https://images.chesscomfiles.com/chess-flair/emoji/flag_ukraine.svg"
          }
        },
        "membership": "basic",
        "country": "UA",
        "timezone": "Europe/London",
        "createdAt": "2020-01-23T08:05:42Z",
        "updatedAt": "2025-10-15T17:55:53.731Z",
        "isEnabled": true,
        "isContentHidden": false,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "flag_ukraine"
      },
      "lastLoginDate": "2025-10-15T17:55:57.780Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_OFFLINE",
      "bestRating": 1422,
      "country": {
        "id": 141,
        "name": "Ukraine",
        "code": "UA"
      },
      "ratings": [
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS960_BLITZ",
          "rating": 1406
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BLITZ",
          "rating": 1336
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_RAPID",
          "rating": 1422
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_BUGHOUSE_BLITZ",
          "rating": 1224
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_THREECHECK_BLITZ",
          "rating": 1091
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BULLET",
          "rating": 905
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_DAILY",
          "rating": 1074
        }
      ]
    },
    {
      "uuid": "e2f67606-d276-11eb-a6e0-9b48fb69b0c5",
      "userView": {
        "id": "e2f67606-d276-11eb-a6e0-9b48fb69b0c5",
        "userId": "145365985",
        "type": "USER_TYPE_USER",
        "username": "MAGNUSCARLSEN_66399",
        "firstName": "Arya",
        "lastName": "Bat",
        "avatar": {
          "url": "https://images.chesscomfiles.com/uploads/v1/user/145365985.b825eda6.30x30o.2e2cd6a76d92.jpeg",
          "highResolutionUrl": "https://images.chesscomfiles.com/uploads/v1/user/145365985.b825eda6.200x200o.d60ad023bc5a.jpeg"
        },
        "membership": "basic",
        "country": "IN",
        "timezone": "America/Los_Angeles",
        "createdAt": "2021-06-21T09:56:02Z",
        "updatedAt": "2025-09-04T13:41:26.880Z",
        "isEnabled": true,
        "isContentHidden": false,
        "isVerified": false,
        "contentLanguageIncludesEnglish": true,
        "flairCode": "nothing"
      },
      "lastLoginDate": "2025-05-14T11:25:01.391Z",
      "presence": "FRIENDS_SEARCH_PRESENCE_STATUS_OFFLINE",
      "bestRating": 1624,
      "country": {
        "id": 69,
        "name": "India",
        "code": "IN"
      },
      "ratings": [
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS960_BLITZ",
          "rating": 1141
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BLITZ",
          "rating": 1297
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_RAPID",
          "rating": 1624
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_BUGHOUSE_BLITZ",
          "rating": 1124
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_THREECHECK_BLITZ",
          "rating": 780
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_BULLET",
          "rating": 1077
        },
        {
          "variantTimeclass": "FRIENDS_SEARCH_VARIANT_TIMECLASS_CHESS_DAILY",
          "rating": 1113
        }
      ]
    }
  ]
}
```
