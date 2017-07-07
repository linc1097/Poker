#include <iostream>
#include <unordered_map>
#include <random>
#include <algorithm>
#include <iterator>

#include "win_perc.h"

namespace {
    struct Card {
        int primeRank;
        int suit;
    };

    int primes[] = { 0, 1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43 };

    inline int handRanking(Card &c1, Card &c2, Card &c3, Card &c4, Card &c5) {
        int r = c1.primeRank *
                c2.primeRank *
                c3.primeRank *
                c4.primeRank *
                c5.primeRank;

        // multiply by 43 if flush
        //r *= 1 + ((c1.suit == c2.suit) &&
        //          (c2.suit == c3.suit) &&
        //          (c3.suit == c4.suit) &&
        //          (c4.suit == c5.suit)) * (43 - 1);
        int b = c1.suit | c2.suit | c3.suit | c4.suit | c5.suit;
        r *= 1 + (b && !(b & (b-1))) * (43 - 1);
        return handRankings.at(r);
    }

    std::string cardRank(Card *card, int idx) {
        int r = std::distance(primes, std::find(primes, primes + 16, card[idx].primeRank));
        if (r < 10)
            return std::to_string(r);
        else if (r == 10)
            return "T";
        else if (r == 11)
            return "J";
        else if (r == 12)
            return "Q";
        else if (r == 13)
            return "K";
        else if (r == 14)
            return "A";
    }

    std::string cardSuit(Card *card, int idx) {
        std::string suits[] = { "", "c", "d", "", "h", "", "", "", "s" };
        return suits[card[idx].suit];
    }

    inline int evaluate(Card* cards, int turn, int river, int aiCard1, int aiCard2) {
        int p1 = 0;
        int p2 = 1;
        int p3 = 2;
        int p4 = 3;
        int p5 = 4;
        int p6 = turn;
        int p7 = river;

        int ai1 = 0;
        int ai2 = 1;
        int ai3 = 2;
        int ai4 = aiCard1;
        int ai5 = aiCard2;
        int ai6 = turn;
        int ai7 = river;

        int r;

        int playerRanking = 0;
        r = handRanking(cards[p1], cards[p2], cards[p3], cards[p4], cards[p5]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p2], cards[p3], cards[p4], cards[p6]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p2], cards[p3], cards[p4], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p2], cards[p3], cards[p5], cards[p6]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p2], cards[p3], cards[p5], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p2], cards[p3], cards[p6], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p2], cards[p4], cards[p5], cards[p6]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p2], cards[p4], cards[p5], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p2], cards[p4], cards[p6], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p2], cards[p5], cards[p6], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p3], cards[p4], cards[p5], cards[p6]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p3], cards[p4], cards[p5], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p3], cards[p4], cards[p6], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p3], cards[p5], cards[p6], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p1], cards[p4], cards[p5], cards[p6], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p2], cards[p3], cards[p4], cards[p5], cards[p6]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p2], cards[p3], cards[p4], cards[p5], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p2], cards[p3], cards[p4], cards[p6], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p2], cards[p3], cards[p5], cards[p6], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p2], cards[p4], cards[p5], cards[p6], cards[p7]); if (r > playerRanking) playerRanking = r;
        r = handRanking(cards[p3], cards[p4], cards[p5], cards[p6], cards[p7]); if (r > playerRanking) playerRanking = r;

        int aiRanking = 0;
        r = handRanking(cards[ai1], cards[ai2], cards[ai3], cards[ai4], cards[ai5]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai2], cards[ai3], cards[ai4], cards[ai6]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai2], cards[ai3], cards[ai4], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai2], cards[ai3], cards[ai5], cards[ai6]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai2], cards[ai3], cards[ai5], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai2], cards[ai3], cards[ai6], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai2], cards[ai4], cards[ai5], cards[ai6]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai2], cards[ai4], cards[ai5], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai2], cards[ai4], cards[ai6], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai2], cards[ai5], cards[ai6], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai3], cards[ai4], cards[ai5], cards[ai6]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai3], cards[ai4], cards[ai5], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai3], cards[ai4], cards[ai6], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai3], cards[ai5], cards[ai6], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai1], cards[ai4], cards[ai5], cards[ai6], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai2], cards[ai3], cards[ai4], cards[ai5], cards[ai6]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai2], cards[ai3], cards[ai4], cards[ai5], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai2], cards[ai3], cards[ai4], cards[ai6], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai2], cards[ai3], cards[ai5], cards[ai6], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai2], cards[ai4], cards[ai5], cards[ai6], cards[ai7]); if (r > aiRanking) aiRanking = r;
        r = handRanking(cards[ai3], cards[ai4], cards[ai5], cards[ai6], cards[ai7]); if (r > aiRanking) aiRanking = r;

        // tie         -> 0
        // player wins -> 1
        // ai     wins -> 0
        //return (playerRanking != aiRanking) * ((playerRanking > aiRanking) * 2 - 1);
        return playerRanking > aiRanking;
    }
}

int main() {
    std::random_device rd;
    //std::mt19937 g(0);
    std::mt19937 g(rd());

    // flop   0, 1, 2
    // player 3, 4
    Card cards[52];

    int idx = 0;
    for (int rank=2; rank<=14; ++rank) {
        for (int suit=1; suit<=4; ++suit) {
            cards[idx] = Card{primes[rank], 1 << (suit - 1)};
            ++idx;
        }
    }

    std::shuffle(&cards[0], &cards[52], g);

    std::cout << "player: " << cardRank(cards, 3) << cardSuit(cards, 3) << " "
                            << cardRank(cards, 4) << cardSuit(cards, 4) << std::endl;
    std::cout << "flop: " << cardRank(cards, 0) << cardSuit(cards, 0) << " "
                          << cardRank(cards, 1) << cardSuit(cards, 1) << " "
                          << cardRank(cards, 2) << cardSuit(cards, 2) << std::endl;

    int wins = 0;
    int num = 0;

    for (int i=5; i<52; ++i) {
        for (int j=i+1; j<52; ++j) {
            for (int k=j+1; k<52; ++k) {
                for (int l=k+1; l<52; ++l) {
                    wins += evaluate(cards, i, j, k, l);
                    wins += evaluate(cards, i, k, j, l);
                    wins += evaluate(cards, i, l, j, k);
                    wins += evaluate(cards, j, k, i, l);
                    wins += evaluate(cards, j, l, i, k);
                    wins += evaluate(cards, k, l, i, j);
                    num += 6;
                }
            }
        }
    }

    std::cout << wins << " / " << num << std::endl;
    std::cout << wins / (double)num << std::endl;

    return 0;
}

