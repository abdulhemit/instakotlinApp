package com.example.ilkacilma.Models

class userDetails {
    var follower : String? = null
    var following : String? = null
    var post : String? = null
    var profile_pucture : String? = null
    var biography : String? = null
    var web_site : String? = null

    constructor(){}
    constructor(
        follower: String?,
        following: String?,
        post: String?,
        profile_pucture: String?,
        biography: String?,
        web_site: String?
    ) {
        this.follower = follower
        this.following = following
        this.post = post
        this.profile_pucture = profile_pucture
        this.biography = biography
        this.web_site = web_site
    }

    override fun toString(): String {
        return "userDetails(follower=$follower, following=$following, post=$post, profile_pucture=$profile_pucture, biography=$biography, web_site=$web_site)"
    }


}