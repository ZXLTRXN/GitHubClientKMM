//
//  RepositoryTableViewCell.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 23.06.2022.
//

import UIKit
import shared

class RepositoryTableViewCell: UITableViewCell {
    
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var languageLabel: UILabel!
    @IBOutlet private weak var descriptionLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func setData(repo: Repo) {
        nameLabel.text = repo.name
        languageLabel.text = repo.language
        descriptionLabel.text = repo.description_
        languageLabel.textColor = ColorManager.shared.getColor(language: repo.language)
    }
}
