import {LikeApi} from '../../build/openapi/apis';
import {Like} from "../../build/openapi/models";



export class LikeService {
    private readonly likeApi: LikeApi;

    constructor() {
        this.likeApi = new LikeApi();
    }

    public postLike(): Promise<Like> {
        return this.likeApi.likePost();
    }

    public getCount(): Promise<object> {
        return this.likeApi.likeCountGet();
    }

}
